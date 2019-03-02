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

import io.agilehandy.common.api.legs.LegAddedEvent;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.model.TransportationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Data
@NoArgsConstructor
public class Leg {

	UUID id;
	UUID bookingId;
	UUID cargoId;
	UUID routeId;

	Location startLocation;
	Location endLocation;
	TransportationType transType;
	LocalDateTime pickupTime;
	LocalDateTime dropOffTime;

	public Leg(UUID legId) {
		this.id = legId;
	}

	public void legAdded(LegAddedEvent event) {
		this.id = event.getLegId();
		this.bookingId = event.getBookingId();
		this.cargoId = event.getCargoId();
		this.routeId = event.getRouteId();
		this.startLocation = event.getStartLocation();
		this.endLocation = event.getEndLocation();
		this.transType = event.getTransType();
	}

}
