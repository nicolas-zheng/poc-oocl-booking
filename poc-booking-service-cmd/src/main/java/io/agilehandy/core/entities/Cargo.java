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

import io.agilehandy.common.api.cargos.CargoAddedEvent;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.ContainerSize;
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
public class Cargo {

	UUID id;
	UUID bookingId;

	CargoNature nature;
	ContainerSize requiredSize;
	ContainerSize assignedSize;

	List<Route> routeList;

	public Cargo(UUID cargoId) {
		this.id = cargoId;
	}

	public void cargoAdded(CargoAddedEvent event) {
		this.setRouteList(new ArrayList<>());
		this.setId(UUID.fromString(event.getCargoId()));
		this.setBookingId(UUID.fromString(event.getBookingId()));
		this.setNature(event.getNature());
		this.setRequiredSize(event.getRequiredSize());
	}

	public void routeAdded(RouteAddedEvent event) {
		Route route = this.routeMember(UUID.fromString(event.getRouteId()));
		route.routeAdded(event);
		this.getRouteList().add(route);
	}

	public Route routeMember(UUID routeId) {
		Route route = getRouteList().stream()
				.filter(r -> r.getId() == routeId)
				.findFirst()
				.orElse(new Route(routeId));
		this.getRouteList().add(route);
		return route;
	}

}
