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


package io.agilehandy.common.api.cargos;

import io.agilehandy.common.api.BookingBaseEvent;
import io.agilehandy.common.api.BookingEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.ContainerSize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class CargoAddedEvent extends BookingBaseEvent implements BookingEvent {

	private String cargoId;

	private CargoNature nature;
	private ContainerSize requiredSize;

	public static class Builder {
		private CargoAddedEvent eventToBuild;

		public Builder() {
			eventToBuild = new CargoAddedEvent();
		}

		public CargoAddedEvent build() {
			eventToBuild.setOccurredOn(LocalDateTime.now());
			eventToBuild.setType(EventTypes.CARGO_ADDED);
			CargoAddedEvent eventBuilt = eventToBuild;
			eventToBuild = new CargoAddedEvent();
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

		public Builder setNature(CargoNature cargoNature) {
			eventToBuild.setNature(cargoNature);
			return this;
		}

		public Builder setRequiredSize(ContainerSize size) {
			eventToBuild.setRequiredSize(size);
			return this;
		}
	}

}
