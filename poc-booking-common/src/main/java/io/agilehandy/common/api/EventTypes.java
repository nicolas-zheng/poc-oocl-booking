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


package io.agilehandy.common.api;

/**
 * @author Haytham Mohamed
 **/

public interface EventTypes {

	String UNKNOWN_EVENT = "UNKNOWN";

	String BOOKING_CREATED = "BOOKING_CREATED";
	String BOOKING_UPDATED = "BOOKING_UPDATED";
	String BOOKING_CANCELED = "BOOKING_CANCELED";

	String CARGO_CREATED = "CARGO_CREATED";
	String CARGO_ADDED = "CARGO_ADDED";
	String CARGO_UPDATED = "CARGO_UPDATED";
	String CARGO_CANCELED = "CARGO_CANCELED";

	String LEG_CREATED = "LEG_CREATED";
	String LEG_ADDED = "LEG_ADDED";
	String LEG_UPDATED = "LEG_UPDATED";
	String LEG_CANCELED = "LEG_CANCELED";

	String ROUTE_CREATED = "ROUTE_CREATED";
	String ROUTE_ADDED = "ROUTE_ADDED";
	String ROUTE_UPDATED = "ROUTE_UPDATED";
	String ROUTE_CANCELED = "ROUTE_CANCELED";

}
