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
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.agilehandy.common.api.BaseEvent;
import io.agilehandy.common.api.EventTypes;
import io.agilehandy.common.api.ParentEvent;
import io.agilehandy.common.api.model.Location;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author Haytham Mohamed
 **/
@Value
public class BookingCreatedEvent extends ParentEvent implements BaseEvent {

	String customerId;
	Location origin;
	Location destination;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	LocalDate cutOffDate;

	public BookingCreatedEvent() {
		this(null, null, null, null, null);
	}

	public BookingCreatedEvent(String bookingId, String customerId, Location origin
			, Location dest, LocalDate cut) {
		super(EventTypes.BOOKING_CREATED);
		this.setBookingId(bookingId);
		this.customerId = customerId;
		this.origin = origin;
		this.destination = dest;
		this.cutOffDate = cut;
	}

}
