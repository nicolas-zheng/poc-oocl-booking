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


package io.agilehandy.web.routes;

import io.agilehandy.common.api.routes.RouteAddCommand;
import io.agilehandy.core.entities.Booking;
import io.agilehandy.core.entities.Route;
import io.agilehandy.web.CommonService;
import io.agilehandy.web.booking.BookingRepository;
import io.agilehandy.web.legs.LegService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/

@Service
public class RouteService {

	private final BookingRepository repository;
	private final LegService legService;

	public RouteService(BookingRepository repository, LegService legService) {
		this.repository = repository;
		this.legService = legService;
	}

	public String addRoute(RouteAddCommand cmd) {
		Booking booking = CommonService.getBookingById(repository, cmd.getBookingId());
		String routeId = booking.addRoute(cmd).toString();
		repository.save(booking);
		return routeId;
	}

	public Route getRoute(String bookingId, String cargoId) {
		Booking booking = CommonService.getBookingById(repository, bookingId);
		return booking.getCargo(UUID.fromString(cargoId)).getRoute();
	}

}
