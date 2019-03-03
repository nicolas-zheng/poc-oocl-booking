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
import io.agilehandy.common.api.legs.LegAddedEvent;
import io.agilehandy.common.api.model.Location;
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

	@JsonIgnore
	UUID bookingId;

	@JsonIgnore
	UUID cargoId;

	Location origin;
	Location destination;

	List<Leg> legList;

	public Route(UUID routeId) {
		this.id = routeId;
	}

	public void routeAdded(RouteAddedEvent event) {
		this.setLegList(new ArrayList<>());
		this.setId(UUID.fromString(event.getRouteId()));
		this.setBookingId(UUID.fromString(event.getBookingId()));
		this.setCargoId(UUID.fromString(event.getCargoId()));
		this.setOrigin(event.getOrigin());
		this.setDestination(event.getDestination());
	}

	public void legAdded(LegAddedEvent event) {
		Leg leg = legMember(UUID.fromString(event.getLegId()));
		leg.legAdded(event);
		this.getLegList().add(leg);
	}

	public Leg legMember(UUID legId) {
		Leg leg = getLegList().stream()
				.filter(l -> l.getId() == legId)
				.findFirst()
				.orElse(new Leg(legId));
		this.getLegList().add(leg);
		return leg;
	}

}
