package com.webfluxdemo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import com.webfluxdemo.models.Event;

import reactor.core.publisher.Flux;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, String>{
	
	@Tailable
	public Flux<Event> findWithTailableCursorBy();
	
	@Tailable
	public Flux<Event> findByType(String type);

}
