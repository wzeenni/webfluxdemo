package com.webfluxdemo.repositories;

import java.util.Date;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import com.webfluxdemo.models.Tweet;

import reactor.core.publisher.Flux;

@Repository
public interface TweetRepository extends ReactiveMongoRepository<Tweet, String> {

	@Tailable
	Flux<Tweet> findWithTailableCursorBy();
	
	@Tailable
	Flux<Tweet> findByUserIdAndActive(Integer id, Boolean active);
	
	@Tailable
	//@Query("{ 'createdAt' : { $gte : CURRENT_DATE  } }")
	Flux<Tweet> findByUserIdAndActiveAndCreatedAtGreaterThan(Integer id, Boolean active, Date date);
	
}