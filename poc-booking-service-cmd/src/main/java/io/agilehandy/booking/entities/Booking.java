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


package io.agilehandy.booking.entities;

import io.agilehandy.common.api.BaseEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.bookings.BookingCreateCommand;
import io.agilehandy.common.api.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.cargos.CargoAddCommand;
import io.agilehandy.common.api.cargos.CargoAddedEvent;
import io.agilehandy.common.api.legs.LegAddCommand;
import io.agilehandy.common.api.legs.LegAddedEvent;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.routes.RouteAddCommand;
import io.agilehandy.common.api.routes.RouteAddedEvent;
import javaslang.API;
import javaslang.Predicates;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javaslang.API.*;

/**
 * @author Haytham Mohamed
 **/

// Aggregate Root

@Data
@Slf4j
public class Booking {

	private List<BaseEvent> cache = new ArrayList<>();

	UUID id;

	UUID customerId;
	Location origin;
	Location destination;
	LocalDateTime cutOffDate;

	List<Cargo> cargoList;
	List<Route> routeList;
	List<Leg> legList;

	public Booking() {}

	public Booking(BookingCreateCommand cmd) {
		// TODO: perform any business validation here
		BookingCreatedEvent event =
				new BookingCreatedEvent(UUID.randomUUID(),
						cmd.getCustomerId(),
						cmd.getOrigin(),
						cmd.getDestination(),
						cmd.getCutOffDate());
		bookingCreated(event);
	}

	public Booking bookingCreated(BookingCreatedEvent event) {
		this.id = event.getSubjectId();
		this.customerId = event.getCustomerId();
		this.cutOffDate = event.getCutOffDate();
		this.origin = event.getOrigin();
		this.destination = event.getDestination();
		cargoList = new ArrayList<>();
		routeList = new ArrayList<>();
		legList = new ArrayList<>();
		this.cacheEvent(event);
		return this;
	}

	public void addCargo(CargoAddCommand cmd) {
		CargoAddedEvent event =
				new CargoAddedEvent(this.getId(), UUID.randomUUID()
						,cmd.getNature(), cmd.getRequiredSize());

		this.cargoAdded(event);
	}

	public Booking cargoAdded(CargoAddedEvent event) {
		cargoMember(event.getCargoId()).cargoAdded(event);
		cacheEvent(event);
		return this;
	}

	public void addRoute(RouteAddCommand cmd) {
		RouteAddedEvent event =
				new RouteAddedEvent(this.getId(), UUID.randomUUID()
						, cmd.getOrigin(), cmd.getDestination());
		this.routeAdded(event);
	}

	public Booking routeAdded(RouteAddedEvent event) {
		routeMember(event.getCargoId(), UUID.randomUUID()).routeAdded(event);
		cacheEvent(event);
		return this;
	}

	public void addLeg(LegAddCommand cmd) {
		LegAddedEvent event =
				new LegAddedEvent(cmd.getSubjectId(), cmd.getCargoId(), cmd.getRouteId(),
						UUID.randomUUID(), cmd.getStartLocation(),
						cmd.getEndLocation(), cmd.getTransportationType());
		this.legAdded(event);
	}

	public Booking legAdded(LegAddedEvent event) {
		legMember(event.getCargoId(), event.getRouteId(), UUID.randomUUID()).legAdded(event);
		cacheEvent(event);
		return this;
	}

	public Booking handleEvent(BaseEvent event) {
		return API.Match(event.getType()).of(
				Case(Predicates.is(EventTypes.BOOKING_CREATED), this.bookingCreated((BookingCreatedEvent) event))
				, Case(Predicates.is(EventTypes.CARGO_ADDED), this.cargoAdded((CargoAddedEvent) event))
				, Case(Predicates.is(EventTypes.ROUTE_ADDED), this.routeAdded((RouteAddedEvent) event))
				, Case(Predicates.is(EventTypes.LEG_ADDED), this.legAdded((LegAddedEvent) event))
		);
	}

	public void cacheEvent(BaseEvent event) {
		cache.add(event);
	}

	public void clearEventCache() {
		this.cache.clear();
	}
	
	public Cargo cargoMember(UUID cargoId) {
		Cargo cargo = cargoList.stream()
				.filter(c -> c.getId() == cargoId)
				.findFirst().orElse(new Cargo(this.getId(), cargoId));
		this.cargoList.add(cargo); // set parent
		return cargo;
	}

	public Route routeMember(UUID cargoId, UUID routeId) {
		Route route = routeList.stream()
				.filter(r -> r.getId() == routeId)
				.findFirst()
				.orElse(new Route(this.getId(), cargoId, routeId));
		this.routeList.add(route);
		return route;
	}

	public Leg legMember(UUID cargoId, UUID routeId, UUID legId) {
		Leg leg = legList.stream()
				.filter(l -> l.getId() == id)
				.findFirst()
				.orElse(new Leg(this.getId(), cargoId, routeId, legId));
		leg.setRoute(routeMember(cargoId, routeId)); // set parent
		this.legList.add(leg);
		return leg;
	}

}
