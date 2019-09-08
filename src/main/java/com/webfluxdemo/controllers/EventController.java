package com.webfluxdemo.controllers;

import java.time.Duration;
import java.time.Instant;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webfluxdemo.models.Event;
import com.webfluxdemo.repositories.EventRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class EventController {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
    ReactiveMongoTemplate reactiveTemplate;

	@PostMapping("event")
	public Mono<Event> newEvent(@Valid @RequestBody Event event) {
		return eventRepository.save(event);
	}


	@GetMapping(value = "/stream/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Event> getAllEvents(@RequestParam(value = "type", required = false) String type) {
				
		if(type == null || type.isBlank() || type.isEmpty()) {
			return eventRepository.findWithTailableCursorBy().delayElements(Duration.ofMillis(100));			
		}
		
		// If "type" is a meeting or event, return only those
		if(type.compareTo("m") == 0 || type.compareTo("e") == 0) {
			
			return eventRepository.findByType(type).delayElements(Duration.ofMillis(100));
			
		}
		
		// Else, return all types of events
		return eventRepository.findWithTailableCursorBy().delayElements(Duration.ofMillis(100));
		
	}

	
	
	@GetMapping(value = "/stream/events2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ChangeStreamEvent<Event>> getAllEvents2() {
		
		//ChangeStreamOptions options = ChangeStreamOptions.builder().build();
		
		ChangeStreamOptions options = ChangeStreamOptions.builder()
			    .resumeAt(Instant.now().minusSeconds(1))        
			    .build();

		Flux<ChangeStreamEvent<Event>> flux = reactiveTemplate.changeStream("event", options, Event.class);
		
		flux.log();
			
		return flux;
		
	}


}
