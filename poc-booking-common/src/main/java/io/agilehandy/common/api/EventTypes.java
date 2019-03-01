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

public interface EventTypes {

	public final static String BOOKING_CREATED = "BOOKING_CREATED";
	public final static String BOOKING_UPDATED = "BOOKING_UPDATED";
	public final static String BOOKING_CANCELED = "BOOKING_CANCELED";

	public final static String CARGO_CREATED = "CARGO_CREATED";
	public final static String CARGO_UPDATED = "CARGO_UPDATED";
	public final static String CARGO_CANCELED = "CARGO_CANCELED";

	public final static String LEG_CREATED = "LEG_CREATED";
	public final static String LEG_UPDATED = "LEG_UPDATED";
	public final static String LEG_CANCELED = "LEG_CANCELED";

	public final static String ROUTE_CREATED = "ROUTE_CREATED";
	public final static String ROUTE_UPDATED = "ROUTE_UPDATED";
	public final static String ROUTE_CANCELED = "ROUTE_CANCELED";

}
