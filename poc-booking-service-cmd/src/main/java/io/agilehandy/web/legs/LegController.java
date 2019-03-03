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
import io.agilehandy.core.entities.Leg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Haytham Mohamed
 **/

@RestController
public class LegController {

	private final LegService service;

	public LegController(LegService service) {
		this.service = service;
	}

	@PostMapping("/path/legs")
	public String addLeg(@RequestBody LegAddCommand cmd) {
		return service.addLeg(cmd);
	}

	@GetMapping("/{bookingId}/cargos/{cargoId}/routes/{routeId}/legs/{legId}")
	public Leg getLeg(@PathVariable String bookingId, @PathVariable String cargoId,
	                  @PathVariable String routeId, @PathVariable String legId) {
		return service.getLeg(bookingId, cargoId, legId);
	}

	@GetMapping("/{bookingId}/cargos/{cargoId}/routes/{routeId}/legs")
	public List<Leg> getLegs(@PathVariable String bookingId, @PathVariable String cargoId,
	                         @PathVariable String routeId) {
		return service.getLegs(bookingId, cargoId);
	}
}
