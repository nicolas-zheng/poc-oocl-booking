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
import io.agilehandy.common.api.legs.LegAddCommand;
import io.agilehandy.common.api.legs.LegAddedEvent;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.model.TransportationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Data
@NoArgsConstructor
public class Leg {

	UUID id;

	Location startLocation;
	Location endLocation;
	TransportationType transType;
	LocalDateTime pickupTime;
	LocalDateTime dropOffTime;

	// parent
	Route route;

	public void add(LegAddCommand cmd) {
		Assert.notNull(route, "Leg should have its parent Route aggregate set");

		LegAddedEvent event =
				new LegAddedEvent(UUID.randomUUID(), route.getCargo().getBooking().getId(),
						route.getCargo().getId(), route.getId(), cmd.getStartLocation(),
						cmd.getEndLocation(), cmd.getTransportationType());
		LegAdded(event);
	}

	private Booking LegAdded(LegAddedEvent event) {
		this.id = event.getSubjectId();
		this.startLocation = event.getStartLocation();
		this.endLocation = event.getEndLocation();
		this.transType = event.getTransType();
		this.cacheEvent(event);
		return route.getCargo().getBooking();
	}

	public void cacheEvent(BaseEvent event) {
		route.getCargo().getBooking().getCache().add(event);
	}

}
