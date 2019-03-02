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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.BookingCommand;
import io.agilehandy.common.api.model.Location;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/
@Data
public class RouteAddCommand implements BookingCommand, Serializable {

	private String bookingId;
	private String cargoId;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime occurredOn;

	private Location origin;
	private Location destination;

	public RouteAddCommand() {
		this.occurredOn = LocalDateTime.now();
	}

	@Data
	public static class Builder {

		private RouteAddCommand commandToBuild;

		public Builder() {
			commandToBuild = new RouteAddCommand();
		}

		public RouteAddCommand build() {
			RouteAddCommand commandBuilt = commandToBuild;
			commandToBuild = new RouteAddCommand();
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

		public Builder setOrigin(Location origin) {
			commandToBuild.setOrigin(origin);
			return this;
		}

		public Builder setDestination(Location destination) {
			commandToBuild.setDestination(destination);
			return this;
		}
	}

}
