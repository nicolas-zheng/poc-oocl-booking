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
import io.agilehandy.common.api.routes.RouteAddedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Data
@NoArgsConstructor
public class Route {

	UUID id;
	UUID bookingId;
	UUID cargoId;

	List<Leg> legList;

	public Route(UUID routeId) {
		this.id = routeId;
	}

	public void routeAdded(RouteAddedEvent event) {
		legList = new ArrayList<>();
		this.id = event.getRouteId();
		this.bookingId = event.getBookingId();
		this.cargoId = event.getCargoId();
	}

	public void legAdded(LegAddedEvent event) {
		Leg leg = legMember(event.getLegId());
		leg.legAdded(event);
		this.legList.add(leg);
	}

	public Leg legMember(UUID legId) {
		Leg leg = legList.stream()
				.filter(l -> l.getId() == id)
				.findFirst()
				.orElse(new Leg(legId));
		this.legList.add(leg);
		return leg;
	}

}
