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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.agilehandy.common.api.BookingBaseEvent;
import io.agilehandy.common.api.BookingEvent;
import io.agilehandy.common.api.EventTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class BookingCreatedEvent extends BookingBaseEvent implements BookingEvent {

	private String customerId;
	private String origin;
	private String destination;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate cutOffDate;

	public BookingCreatedEvent(String bookingId,String customerId,String origin
			,String destintation,LocalDate cutOffDate) {
		super(bookingId, EventTypes.BOOKING_CREATED);
		this.setBookingId(bookingId);
		this.customerId = customerId;
		this.origin = origin;
		this.destination = destination;
		this.cutOffDate = cutOffDate;
	}

}
