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


package io.agilehandy.web;

import io.agilehandy.common.api.exceptions.BookingNotFoundException;
import io.agilehandy.core.entities.Booking;
import io.agilehandy.web.booking.BookingRepository;

/**
 * @author Haytham Mohamed
 **/
public class CommonService {

	public static Booking getBookingById(BookingRepository repository, String bookingId) {
		Booking booking = repository.findById(bookingId.toString());
		if (booking == null) {
			throw new BookingNotFoundException(
					String.format("Booking with id %s is not found", bookingId));
		}
		return  booking;
	}
}
