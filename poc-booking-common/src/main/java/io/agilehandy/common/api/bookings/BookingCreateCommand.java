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


package io.agilehandy.common.api.bookings;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.BookingCommand;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.CargoRequest;
import io.agilehandy.common.api.model.ContainerSize;
import io.agilehandy.common.api.model.Location;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Haytham Mohamed
 **/

@Data
public class BookingCreateCommand implements BookingCommand, Serializable {

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime occurredOn;

	private String customerId;
	private Location origin;
	private Location destination;

	List<CargoRequest> cargoRequests;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cutOffDate;

	public BookingCreateCommand() {
		this.occurredOn = LocalDateTime.now();
	}


	public static class Builder {

		private BookingCreateCommand commandToBuild;

		public Builder() {
			commandToBuild = new BookingCreateCommand();
		}

		public BookingCreateCommand build() {
			BookingCreateCommand commandBuilt = commandToBuild;
			commandToBuild = new BookingCreateCommand();
			return commandBuilt;
		}

		public Builder setCustomerId(String customerId) {
			commandToBuild.setCustomerId(customerId);
			return this;
		}

		public Builder setOrigin(Location origin) {
			commandToBuild.setOrigin(origin);
			return this;
		}

		public Builder setDestination(Location destination) {
			commandToBuild.setDestination(destination);
			return this;
		}

		public Builder setCutOffDate(LocalDateTime cutoffDate) {
			commandToBuild.setCutOffDate(cutoffDate);
			return this;
		}

		public Builder requestCargo(ContainerSize size, CargoNature nature) {
			if (commandToBuild.getCargoRequests() != null) {
				commandToBuild.setCargoRequests(new ArrayList<>());
			}
			commandToBuild.getCargoRequests().add(new CargoRequest(size, nature));
			return this;
		}
	}

}
