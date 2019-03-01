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
import io.agilehandy.common.api.routes.RouteAddCommand;
import io.agilehandy.common.api.routes.RouteAddedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Data
@NoArgsConstructor
public class Route {

	UUID id;

	// parent
	Cargo cargo;

	public void add(RouteAddCommand cmd) {
		Assert.notNull(cargo, "Route should have its parent Cargo aggregate set");

		RouteAddedEvent event =
				new RouteAddedEvent(UUID.randomUUID(), cargo.getBooking().getId()
						, cargo.getId(), cmd.getOrigin(), cmd.getDestination());
		RouteAdded(event);
}

	private Booking RouteAdded(RouteAddedEvent event) {
		this.id = event.getSubjectId();
		this.cacheEvent(event);
		return cargo.getBooking();
	}

	public void cacheEvent(BaseEvent event) {
		cargo.getBooking().getCache().add(event);
	}
}
