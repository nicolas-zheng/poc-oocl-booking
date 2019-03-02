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


package io.agilehandy.web.cargos;

import io.agilehandy.common.api.cargos.CargoAddCommand;
import io.agilehandy.core.entities.Booking;
import io.agilehandy.core.entities.Cargo;
import io.agilehandy.web.CommonService;
import io.agilehandy.web.booking.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/

@Service
public class CargoService {

	private final BookingRepository repository;

	public CargoService(BookingRepository repository) {
		this.repository = repository;
	}

	public String addCargo(CargoAddCommand cmd) {
		Booking booking = CommonService.getBookingById(repository, cmd.getBookingId());
		String cargoId = booking.addCargo(cmd).toString();
		repository.save(booking);
		return cargoId;
	}

	public List<Cargo> getCargos(String bookingId) {
		return CommonService.getBookingById(repository, UUID.fromString(bookingId)).getCargoList();
	}

	public Cargo getCargo(String bookingId, String cargoId) {
		Booking booking = CommonService.getBookingById(repository, UUID.fromString(bookingId));
		return booking.getCargo(UUID.fromString(cargoId));
	}
}
