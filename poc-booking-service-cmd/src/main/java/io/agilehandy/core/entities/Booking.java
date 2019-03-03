/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.agilehandy.core.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.BookingEvent;
import io.agilehandy.common.api.bookings.BookingCreateCommand;
import io.agilehandy.common.api.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.cargos.CargoAddCommand;
import io.agilehandy.common.api.cargos.CargoAddedEvent;
import io.agilehandy.common.api.exceptions.CargoNotFoundException;
import io.agilehandy.common.api.exceptions.LegNotFoundException;
import io.agilehandy.common.api.exceptions.RouteNotFoundException;
import io.agilehandy.common.api.legs.LegAddCommand;
import io.agilehandy.common.api.legs.LegAddedEvent;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.routes.RouteAddCommand;
import io.agilehandy.common.api.routes.RouteAddedEvent;
import javaslang.API;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javaslang.API.*;
import static javaslang.Predicates.*;

/**
 * @author Haytham Mohamed
 **/

// Aggregate Root

@Data
@Slf4j
public class Booking {

	@JsonIgnore
	private List<BookingEvent> cache = new ArrayList<>();

	UUID id;

	UUID customerId;
	Location origin;
	Location destination;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime cutOffDate;

	List<Cargo> cargoList;

	public Booking() {}

	public Booking(BookingCreateCommand cmd) {
		// TODO: perform any business validation here
		BookingCreatedEvent event =
				new BookingCreatedEvent(UUID.randomUUID().toString(),
						cmd.getCustomerId(),
						cmd.getOrigin(),
						cmd.getDestination(),
						cmd.getCutOffDate());
		bookingCreated(event);
	}

	public Booking bookingCreated(BookingCreatedEvent event) {
		this.id = UUID.fromString(event.getBookingId());
		this.customerId = UUID.fromString(event.getCustomerId());
		this.cutOffDate = event.getCutOffDate();
		this.origin = new Location(event.getOrigin());
		this.destination = new Location(event.getDestination());
		cargoList = new ArrayList<>();
		this.cacheEvent(event);
		return this;
	}

	public UUID addCargo(CargoAddCommand cmd) {
		UUID cargoId = UUID.randomUUID();
		CargoAddedEvent event =
			new CargoAddedEvent(cmd.getBookingId(), cargoId.toString()
					,cmd.getNature(), cmd.getRequiredSize());

		this.cargoAdded(event);
		return cargoId;
	}

	public Booking cargoAdded(CargoAddedEvent event) {
		Cargo cargo = cargoMember(UUID.fromString(event.getCargoId()));
		cargo.cargoAdded(event);
		this.cargoList.add(cargo);
		cacheEvent(event);
		return this;
	}

	public UUID addRoute(RouteAddCommand cmd) {
		UUID routeId = UUID.randomUUID();
		RouteAddedEvent event =
			new RouteAddedEvent(this.getId().toString(), cmd.getCargoId()
					, routeId.toString(), cmd.getOrigin(), cmd.getDestination());
		this.routeAdded(event);
		return routeId;
	}

	public Booking routeAdded(RouteAddedEvent event) {
		cargoMember(UUID.fromString(event.getCargoId())).routeAdded(event);
		cacheEvent(event);
		return this;
	}

	public UUID addLeg(LegAddCommand cmd) {
		UUID legId = UUID.randomUUID();
		LegAddedEvent event =
				new LegAddedEvent(cmd.getBookingId(), cmd.getCargoId(), cmd.getRouteId(),
						legId.toString(), cmd.getStartLocation(),
						cmd.getEndLocation(), cmd.getTransType());
		this.legAdded(event);
		return legId;
	}

	public Booking legAdded(LegAddedEvent event) {
		cargoMember(UUID.fromString(event.getCargoId()))
						.routeMember(UUID.fromString(event.getRouteId()))
						.legAdded(event);
		cacheEvent(event);
		return this;
	}

	public Booking handleEvent(BookingEvent event) {
		return API.Match(event).of(
				Case( $( instanceOf( BookingCreatedEvent.class ) ), this::bookingCreated)
				, Case( $( instanceOf( CargoAddedEvent.class ) ), this::cargoAdded)
				, Case( $( instanceOf( RouteAddedEvent.class ) ), this::routeAdded)
				, Case( $( instanceOf( LegAddedEvent.class ) ), this::legAdded)
		);
	}

	public void cacheEvent(BookingEvent event) {
		cache.add(event);
	}

	public void clearEventCache() {
		this.cache.clear();
	}

	public Cargo getCargo(UUID cargoId) {
		return cargoList.stream()
				.filter(c -> c.getId() == cargoId)
				.findFirst()
				.orElseThrow(() -> new CargoNotFoundException(
						String.format("No Cargo found with %s ", cargoId)));
	}

	public Route getRoute(UUID cargoId, UUID routeId) {
		Cargo cargo = getCargo(cargoId);
		return cargo.getRouteList().stream().filter(r -> r.id == routeId)
				.findFirst()
				.orElseThrow(() ->
						new RouteNotFoundException(String.format(
							"No Route found with id %s for Cargo id %s ", routeId, cargoId)));
	}

	public Leg getLeg(UUID cargoId, UUID routeId, UUID legId) {
		Route route = getRoute(cargoId, routeId);
		return route.getLegList().stream().filter(l -> l.id == legId)
				.findFirst()
				.orElseThrow(() ->
						new LegNotFoundException(String.format(
							"No Leg found with id %s for Cargo id %s and Route id %s"
								, legId, cargoId, routeId)));
	}

	public Cargo cargoMember(UUID cargoId) {
		Cargo cargo = cargoList.stream()
				.filter(c -> c.getId() == cargoId)
				.findFirst().orElse(new Cargo(cargoId));
		return cargo;
	}

}
