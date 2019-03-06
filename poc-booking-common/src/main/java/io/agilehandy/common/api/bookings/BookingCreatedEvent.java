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
import io.agilehandy.common.api.BookingBaseEvent;
import io.agilehandy.common.api.BookingEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.model.CargoRequest;
import io.agilehandy.common.api.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class BookingCreatedEvent extends BookingBaseEvent implements BookingEvent {

	private String customerId;
	private Location origin;
	private Location destination;
	private List<CargoRequest> cargoRequests;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cutOffDate;

	public static class Builder {
		private BookingCreatedEvent eventToBuild;

		public Builder() {
			eventToBuild = new BookingCreatedEvent();
		}

		public BookingCreatedEvent build() {
			eventToBuild.setOccurredOn(LocalDateTime.now());
			eventToBuild.setType(EventTypes.BOOKING_CREATED);
			BookingCreatedEvent eventBuilt = eventToBuild;
			eventToBuild = new BookingCreatedEvent();
			return eventBuilt;
		}

		public Builder setBookingId(String bookingId) {
			eventToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setCustomerId(String customerId) {
			eventToBuild.setCustomerId(customerId);
			return this;
		}

		public Builder setOrigin(Location origin) {
			eventToBuild.setOrigin(origin);
			return this;
		}

		public Builder setDestination(Location destination) {
			eventToBuild.setDestination(destination);
			return this;
		}

		public Builder setCutOffDate(LocalDateTime date) {
			eventToBuild.setCutOffDate(date);
			return this;
		}

		public Builder setCargoRequests(List<CargoRequest> requests) {
			eventToBuild.setCargoRequests(requests);
			return this;
		}

	}

}
