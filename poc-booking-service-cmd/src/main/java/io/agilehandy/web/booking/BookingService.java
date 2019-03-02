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


package io.agilehandy.web.booking;

import io.agilehandy.common.api.bookings.BookingCreateCommand;
import io.agilehandy.core.entities.Booking;
import io.agilehandy.web.CommonService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/
@Service
public class BookingService {

	private final BookingRepository repository;

	public BookingService(BookingRepository repository) {
		this.repository = repository;
	}

	public String createBooking(BookingCreateCommand cmd) {
		Booking booking = new Booking(cmd);
		repository.save(booking);
		return booking.getId().toString();
	}

	public Booking getBookingById(String bookingId) {
		return CommonService.getBookingById(repository, UUID.fromString(bookingId).toString());
	}

}
