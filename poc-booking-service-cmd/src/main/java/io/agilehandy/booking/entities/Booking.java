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
import io.agilehandy.common.api.legs.LegAddCommand;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.routes.RouteAddCommand;
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
		newCargoMember().add(cmd);
	}

	public void addRoute(Cargo cargo, RouteAddCommand cmd) {
		newRouteMember(cargo).add(cmd);
	}

	public void addLeg(Route route, LegAddCommand cmd) {
		newLegMember(route).add(cmd);
	}

	public Booking handleEvent(BaseEvent event) {
		return API.Match(event.getType()).of(
				Case(Predicates.is(EventTypes.BOOKING_CREATED), this.bookingCreated((BookingCreatedEvent) event))
				//, Case(Predicates.is(EventTypes.BOOKING_UPDATED), this.pikeRented((BikeRentedEvent) event))
				//, Case(Predicates.is(EventTypes.BOOKING_CANCELED), this.pikeReturned((BikeReturnedEvent) event))
		);
	}

	public void cacheEvent(BaseEvent event) {
		cache.add(event);
	}

	public void clearEventCache() {
		this.cache.clear();
	}


	public Cargo newCargoMember() {
		Cargo cargo = new Cargo();
		cargo.setBooking(this);
		this.cargoList.add(cargo); // set parent
		return cargo;
	}

	public Route newRouteMember(Cargo cargo) {
		Route route = new Route();
		route.setCargo(cargo); // set parent
		this.routeList.add(route);
		return route;
	}

	public Leg newLegMember(Route route) {
		Leg leg = new Leg();
		leg.setRoute(route); // set parent
		this.legList.add(leg);
		return leg;
	}

	public Cargo cargoMember(UUID cargoId) {
		return cargoList.stream()
				.filter(c -> c.getBooking().getId() == getId() && c.getId() == cargoId)
				.findFirst().orElse(new Cargo());
	}

	public Route routeMember(UUID routeId) {
		return routeList.stream()
				.filter(r -> r.getId() == routeId)
				.findFirst()
				.orElse(new Route());
	}

	public Leg legMember(UUID legId) {
		return legList.stream()
				.filter(l -> l.getId() == id)
				.findFirst()
				.orElse(new Leg());
	}

}
