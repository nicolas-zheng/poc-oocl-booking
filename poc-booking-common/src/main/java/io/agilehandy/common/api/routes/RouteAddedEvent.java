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


package io.agilehandy.common.api.routes;

import io.agilehandy.common.api.BookingBaseEvent;
import io.agilehandy.common.api.BookingEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class RouteAddedEvent extends BookingBaseEvent implements BookingEvent {

	private String cargoId;
	private String routeId;

	private Location origin;
	private Location destination;

	public static class Builder {
		private RouteAddedEvent eventToBuild;

		public Builder() {
			eventToBuild = new RouteAddedEvent();
		}

		public RouteAddedEvent build() {
			eventToBuild.setOccurredOn(LocalDateTime.now());
			eventToBuild.setType(EventTypes.ROUTE_ADDED);
			RouteAddedEvent eventBuilt = eventToBuild;
			eventToBuild = new RouteAddedEvent();
			return eventBuilt;
		}

		public Builder setBookingId(String bookingId) {
			eventToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setCargoId(String cargoId) {
			eventToBuild.setCargoId(cargoId);
			return this;
		}

		public Builder setRouteId(String routeId) {
			eventToBuild.setRouteId(routeId);
			return this;
		}

		public Builder setOrigin(String opZone, String facility) {
			eventToBuild.setOrigin(new Location(opZone, facility));
			return this;
		}

		public Builder setOrigin(Location location) {
			eventToBuild.setOrigin(location);
			return this;
		}

		public Builder setDestination(String opZone, String facility) {
			eventToBuild.setDestination(new Location(opZone, facility));
			return this;
		}

		public Builder setDestination(Location location) {
			eventToBuild.setDestination(location);
			return this;
		}
	}

}
