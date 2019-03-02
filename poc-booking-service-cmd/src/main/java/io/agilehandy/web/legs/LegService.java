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


package io.agilehandy.web.legs;

import io.agilehandy.common.api.legs.LegAddCommand;
import io.agilehandy.core.entities.Booking;
import io.agilehandy.core.entities.Leg;
import io.agilehandy.web.CommonService;
import io.agilehandy.web.booking.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/

@Service
public class LegService {

	private final BookingRepository repository;

	public LegService(BookingRepository repository) {
		this.repository = repository;
	}

	public String addLeg(LegAddCommand cmd) {
		Booking booking = CommonService.getBookingById(repository, cmd.getBookingId());
		String legId = booking.addLeg(cmd).toString();
		repository.save(booking);
		return legId;
	}

	public List<Leg> getLegs(String bookingId, String cargoId, String routeId) {
		Booking booking = CommonService.getBookingById(repository, bookingId);
		return booking.getRoute(UUID.fromString(cargoId), UUID.fromString(routeId)).getLegList();
	}

	public Leg getLeg(String bookingId, String cargoId, String routeId, String legId) {
		Booking booking = CommonService.getBookingById(repository, bookingId);
		return booking.getLeg(UUID.fromString(cargoId), UUID.fromString(routeId)
				, UUID.fromString(legId));
	}
}
