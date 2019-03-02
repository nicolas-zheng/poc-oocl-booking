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

import io.agilehandy.common.api.BaseCommand;
import io.agilehandy.common.api.ParentCommand;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.ContainerSize;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Haytham Mohamed
 **/
@Data
public class CargoAddCommand extends ParentCommand implements BaseCommand, Serializable {

	String bookingId;

	CargoNature nature;
	ContainerSize requiredSize;

	@Data
	public static class Builder {

		private CargoAddCommand commandToBuild;

		public Builder() {
			commandToBuild = new CargoAddCommand();
		}

		public CargoAddCommand build() {
			CargoAddCommand commandBuilt = commandToBuild;
			commandToBuild = new CargoAddCommand();
			return commandBuilt;
		}

		public Builder setBookingId(String bookingId) {
			commandToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setNature(CargoNature nature) {
			commandToBuild.setNature(nature);
			return this;
		}

		public Builder setRequiredSize(ContainerSize containerSize) {
			commandToBuild.setRequiredSize(containerSize);
			return this;
		}
	}

}
