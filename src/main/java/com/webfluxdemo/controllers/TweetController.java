package com.webfluxdemo.controllers;

import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import com.webfluxdemo.models.Tweet;
import com.webfluxdemo.repositories.TweetRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;

import javax.validation.Valid;

@RestController
public class TweetController {

	@Autowired
	private TweetRepository tweetRepository;

	@GetMapping("/tweets")
	public Flux<Tweet> getAllTweets() {
		return tweetRepository.findAll();
	}

	@PostMapping("/tweets")
	public Mono<Tweet> createTweets(@Valid @RequestBody Tweet tweet) {
		return tweetRepository.save(tweet);
	}

	@GetMapping("/tweets/{id}")
	public Mono<ResponseEntity<Tweet>> getTweetById(@PathVariable(value = "id") String tweetId) {
		return tweetRepository.findById(tweetId)
				.map(savedTweet -> ResponseEntity.ok(savedTweet))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/tweets/{id}")
	public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable(value = "id") String tweetId,
			@Valid @RequestBody Tweet tweet) {
		return tweetRepository.findById(tweetId)
				.flatMap(existingTweet -> {
					existingTweet.setText(tweet.getText());
					return tweetRepository.save(existingTweet);
				})
				.map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/tweets/toggle/{id}")
	public Mono<ResponseEntity<Tweet>> disableTweet(@PathVariable(value = "id") String tweetId) {
		return tweetRepository.findById(tweetId)
				.flatMap(existingTweet -> {
					existingTweet.setActive(!existingTweet.getActive());
					return tweetRepository.save(existingTweet);
				})
				.map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/tweets/{id}")
	public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String tweetId) {

		return tweetRepository.findById(tweetId)
				.flatMap(existingTweet ->
				tweetRepository.delete(existingTweet)
				.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
						)
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}


	
	
	
	
	// Tweets are Sent to the client as Server Sent Events
	@GetMapping(value = "/stream/tweets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Tweet> streamAllTweets(@RequestParam Integer userId) {
		
		//return tweetRepository.findWithTailableCursorBy();
	
		
		
		//return tweetRepository.findByUserIdAndActive(userId, true);//.doOnNext(event -> System.out.println("some event"));
		
		Date today = new Date();
		today.setHours(0);
		
		
		
		return tweetRepository.findByUserIdAndActiveAndCreatedAtGreaterThan(userId, true, today);
		
	}


	
	
	
	
	


	@GetMapping(path = "/stream/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> streamFlux() {
		return Flux.interval(Duration.ofSeconds(1))
			      .map(sequence -> ServerSentEvent.<String> builder()
			        .id(String.valueOf(sequence))
			          .event("periodic-event")
			          .data("SSE - " + LocalTime.now().toString())
			          .build());
	}



}
