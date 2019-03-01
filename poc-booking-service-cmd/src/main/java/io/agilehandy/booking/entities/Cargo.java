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
import io.agilehandy.common.api.cargos.CargoAddCommand;
import io.agilehandy.common.api.cargos.CargoAddedEvent;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.ContainerSize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Data
@NoArgsConstructor
public class Cargo {

	UUID id;

	CargoNature nature;
	ContainerSize requiredSize;
	ContainerSize assignedSize;

	// parent
	Booking booking;

	public void add(CargoAddCommand cmd) {
		Assert.notNull(booking, "Cargo should have its parent Booking aggregate set");

		CargoAddedEvent event =
				new CargoAddedEvent(UUID.randomUUID(), booking.getId()
						,cmd.getNature(), cmd.getRequiredSize());

		cargoAdded(event);
	}

	private Booking cargoAdded(CargoAddedEvent event) {
		this.id = event.getSubjectId();
		this.nature = event.getNature();
		this.requiredSize = event.getRequiredSize();
		this.cacheEvent(event);
		return booking;
	}

	public void cacheEvent(BaseEvent event) {
		booking.getCache().add(event);
	}


}
