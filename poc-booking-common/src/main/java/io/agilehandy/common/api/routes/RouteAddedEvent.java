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

import io.agilehandy.common.api.BaseEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.ParentEvent;
import io.agilehandy.common.api.model.Location;
import lombok.Value;

import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Value
public class RouteAddedEvent extends ParentEvent implements BaseEvent {

	UUID bookingId;
	UUID cargoId;

	Location origin;
	Location destination;

	public RouteAddedEvent(UUID subjectId, UUID bookingId, UUID cargoId, Location origin, Location dest) {
		super(subjectId, EventTypes.ROUTE_ADDED);
		this.bookingId = bookingId;
		this.cargoId = cargoId;
		this.origin = origin;
		this.destination = dest;
	}

}