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


package io.agilehandy.booking.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.agilehandy.booking.entities.Booking;
import io.agilehandy.common.api.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Haytham Mohamed
 **/
@Component
@EnableBinding(BookingEventChannels.class)
@Slf4j
public class BookingEventPubSub {

	private final BookingEventChannels channels;

	public static final String EVENTS_SNAPSHOT = "booking_snapshots";

	private final String HEADER_EVENT_TYPE = "event_type";

	public BookingEventPubSub(BookingEventChannels channels) {
		this.channels = channels;
	}

	public void publish(BaseEvent event) {
		Message<BaseEvent> message = MessageBuilder
				.withPayload(event)
				.setHeader(KafkaHeaders.MESSAGE_KEY, event.getBookingId().toString().getBytes())
				.setHeader(HEADER_EVENT_TYPE, event.getType())
				.build();
		log.info("start publishing create pike event..");
		channels.output().send(message);
		log.info("finish publishing create pike event..");
	}

	// Kafka KTable of aggregate snapshot
	@StreamListener(BookingEventChannels.BOOKING_EVENTS_IN)
	public void snapshot(KStream<String, BaseEvent> events) {
		Serde<BaseEvent> pikeEventSerde = new JsonSerde<>( BaseEvent.class, new ObjectMapper() );
		Serde<Booking> pikeSerde = new JsonSerde<>( Booking.class, new ObjectMapper() );

		events
				//.groupBy( (s, event) -> event.getEventSubject(),
				//Serialized.with(Serdes.String(), pikeEventSerde) )
				.groupByKey()
				.aggregate(Booking::new, (key, event, booking) -> ((Booking) booking).handleEvent(event),
						Materialized.<String, Booking, KeyValueStore<Bytes, byte[]>>as(EVENTS_SNAPSHOT)
								.withKeySerde(Serdes.String())
								.withValueSerde(pikeSerde)
				);
	}

}
