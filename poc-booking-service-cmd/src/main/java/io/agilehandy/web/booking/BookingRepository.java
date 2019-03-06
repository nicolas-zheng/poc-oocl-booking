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

import io.agilehandy.core.entities.Booking;
import io.agilehandy.core.pubsub.BookingEventPubSub;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haytham Mohamed
 **/

@Component
public class BookingRepository {

	private final BookingEventPubSub pubsub;
	private final QueryableStoreRegistry queryableStoreRegistry;

	public BookingRepository(BookingEventPubSub pubsub
			, QueryableStoreRegistry queryableStoreRegistry) {
		this.pubsub = pubsub;
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	public void save(Booking booking) {
		booking.getCache().stream().forEach(e -> pubsub.publish(e));
		booking.clearEventCache();
	}

	public Booking findById(String id) {
		ReadOnlyKeyValueStore<String, Booking> queryStore =
				queryableStoreRegistry.getQueryableStoreType(BookingEventPubSub.EVENTS_SNAPSHOT
						, QueryableStoreTypes.<String, Booking>keyValueStore());
		return queryStore.get(id);
	}

	public List<Booking> findAll() {
		ReadOnlyKeyValueStore<String, Booking> queryStore =
				queryableStoreRegistry.getQueryableStoreType(BookingEventPubSub.EVENTS_SNAPSHOT
						, QueryableStoreTypes.<String, Booking>keyValueStore());
		KeyValueIterator<String, Booking> all = queryStore.all();
		List<Booking> bookings = new ArrayList<>();
		while(all.hasNext()) {
			bookings.add(all.next().value);
		}
		return bookings;
	}
}
