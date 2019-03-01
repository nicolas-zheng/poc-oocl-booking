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


package io.agilehandy.booking.cmd;

import io.agilehandy.common.api.BaseEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.bookings.BookingCreateCommand;
import io.agilehandy.common.api.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.model.Cargo;
import io.agilehandy.common.api.model.Location;
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

	String id;

	String customerId;
	Location origin;
	Location destination;
	LocalDateTime cutOffDate;

	List<Cargo> cargoList;
	List<Route> routeList;
	List<Leg> LegList;

	public Booking() {}

	public Booking(BookingCreateCommand cmd) {
		// TODO: perform any business validation here

		BookingCreatedEvent event =
				new BookingCreatedEvent(UUID.randomUUID().toString(),
						LocalDateTime.now(),
						cmd.getCustomerId(),
						cmd.getOrigin(),
						cmd.getDestination(),
						cmd.getCutOffDate(),
						cmd.getCargoList());

		bookingCreated(event);
	}

	public Booking bookingCreated(BookingCreatedEvent event) {
		this.id = event.getSubjectId();
		this.customerId = event.getCustomerId();
		this.cutOffDate = event.getCutOffDate();
		this.origin = event.getOrigin();
		this.destination = event.getDestination();
		cargoList = new ArrayList<>();
		event.getCargoList().stream().forEach(cargo -> cargoList.add(cargo));
		this.cacheEvent(event);
		return this;
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
}
