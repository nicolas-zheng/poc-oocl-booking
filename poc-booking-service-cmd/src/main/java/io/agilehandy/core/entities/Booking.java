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

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import io.agilehandy.common.api.routes.RouteAddCommand;
import io.agilehandy.common.api.routes.RouteAddedEvent;
import javaslang.API;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

	List<Cargo> cargoList;

	public Booking() {}

	public Booking(BookingCreateCommand cmd) {
		// TODO: perform any invariant rules here
		UUID bookingId = UUID.randomUUID();
		BookingCreatedEvent event =
				new BookingCreatedEvent.Builder()
				.setBookingId(bookingId.toString())
				.setCustomerId(UUID.randomUUID().toString())
				.setCargoRequests(cmd.getCargoRequests())
				.build();
		this.bookingCreated(event);

		// create cargo command for every requested cargo by customer
		if (cmd.getCargoRequests() != null && !cmd.getCargoRequests().isEmpty()) {
			cmd.getCargoRequests().stream().forEach(cargoRequest -> {
				CargoAddCommand cargoAddCommand =
						new CargoAddCommand.Builder()
								.setBookingId(bookingId.toString())
								.setRequiredSize(cargoRequest.getRequiredSize())
								.setNature(cargoRequest.getNature())
								.setCutOffDate(cargoRequest.getCutOffDate())
								.setOrigin(cargoRequest.getOrigin())
								.setDestination(cargoRequest.getDestination())
								.build();
				this.addCargo(cargoAddCommand);
			});
		}
	}

	public Booking bookingCreated(BookingCreatedEvent event) {
		this.setId(UUID.fromString(event.getBookingId()));
		this.setCustomerId(UUID.fromString(event.getCustomerId()));
		this.setCargoList(new ArrayList<>());
		this.cacheEvent(event);
		return this;
	}

	public UUID addCargo(CargoAddCommand cmd) {
		// TODO: perform any invariant rules here
		UUID cargoId = UUID.randomUUID();
		CargoAddedEvent event =
				new CargoAddedEvent.Builder()
				.setBookingId(cmd.getBookingId())
				.setCargoId(cargoId.toString())
				.setNature(cmd.getNature())
				.setRequiredSize(cmd.getRequiredSize())
				.setOrigin(cmd.getOrigin())
				.setDestination(cmd.getDestination())
				.setCutOffDate(cmd.getCutOffDate())
				.build();
		this.cargoAdded(event);
		return cargoId;
	}

	public Booking cargoAdded(CargoAddedEvent event) {
		Cargo cargo = this.cargoMember(UUID.fromString(event.getCargoId()));
		cargo.cargoAdded(event);
		this.getCargoList().add(cargo);
		this.cacheEvent(event);
		return this;
	}

	public UUID addRoute(RouteAddCommand cmd) {
		// TODO: perform any invariant rules here
		UUID routeId = UUID.randomUUID();
		RouteAddedEvent event =
				new RouteAddedEvent.Builder()
				.setBookingId(cmd.getBookingId())
				.setCargoId(cmd.getCargoId())
				.setRouteId(routeId.toString())
				.setOrigin(cmd.getOrigin())
				.setDestination(cmd.getDestination())
				.build();
		this.routeAdded(event);
		return routeId;
	}

	public Booking routeAdded(RouteAddedEvent event) {
		this.cargoMember(UUID.fromString(event.getCargoId())).routeAdded(event);
		this.cacheEvent(event);
		return this;
	}

	public UUID addLeg(LegAddCommand cmd) {
		// TODO: perform any invariant rules here
		UUID legId = UUID.randomUUID();
		LegAddedEvent event =
				new LegAddedEvent.Builder()
				.setBookingId(cmd.getBookingId())
				.setCargoId(cmd.getCargoId())
				.setRouteId(cmd.getRouteId())
				.setLegId(legId.toString())
				.setEndLocation(cmd.getEndLocation())
				.setStartLocation(cmd.getStartLocation())
				.setTransportationType(cmd.getTransType())
				.build();
		this.legAdded(event);
		return legId;
	}

	public Booking legAdded(LegAddedEvent event) {
		this.cargoMember(UUID.fromString(event.getCargoId()))
				.getRoute()
				.legAdded(event);
		this.cacheEvent(event);
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

	public Route getRoute(UUID cargoId) {
		Cargo cargo = this.getCargo(cargoId);
		Route route = cargo.getRoute();
		if (route == null) {
			throw new RouteNotFoundException(String.format(
					"No Route found for Cargo id %s ", cargoId));
		}
		return route;
	}

	public Leg getLeg(UUID cargoId, UUID legId) {
		Route route = this.getRoute(cargoId);
		return route.getLegList().stream().filter(l -> l.id == legId)
				.findFirst()
				.orElseThrow(() ->
						new LegNotFoundException(String.format(
							"No Leg found with id %s for Cargo id %s and Route id %s"
								, legId, cargoId, route.getId())));
	}

	public Cargo cargoMember(UUID cargoId) {
		Cargo cargo = getCargoList().stream()
				.filter(c -> c.getId() == cargoId)
				.findFirst().orElse(new Cargo(cargoId));
		return cargo;
	}

}
