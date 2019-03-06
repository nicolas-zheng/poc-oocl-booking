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


package io.agilehandy.common.api.legs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.BookingCommand;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.model.TransportationType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/

@Data
public class LegAddCommand implements BookingCommand, Serializable {

	private String bookingId;
	private String cargoId;
	private String routeId;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime occurredOn;

	private Location startLocation;
	private Location endLocation;
	private TransportationType transType;

	public LegAddCommand() {
		this.occurredOn = LocalDateTime.now();
	}

	public static class Builder {
		private LegAddCommand commandToBuild;

		public Builder() {
			commandToBuild = new LegAddCommand();
		}

		public LegAddCommand build() {
			LegAddCommand commandBuilt = commandToBuild;
			commandToBuild = new LegAddCommand();
			return commandBuilt;
		}

		public Builder setBookingId(String bookingId) {
			commandToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setCargoId(String cargoId) {
			commandToBuild.setCargoId(cargoId);
			return this;
		}

		public Builder setRouteId(String routeId) {
			commandToBuild.setRouteId(routeId);
			return this;
		}

		public Builder setStartLocation(Location startLocation) {
			commandToBuild.setStartLocation(startLocation);
			return this;
		}

		public Builder setEndLocation(Location endLocation) {
			commandToBuild.setEndLocation(endLocation);
			return this;
		}

		public Builder setTransType(TransportationType transType) {
			commandToBuild.setTransType(transType);
			return this;
		}
	}

}
