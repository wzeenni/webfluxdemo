package com.webfluxdemo.models;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="users")
public class User {
	
	@Id
	private String userId;
	
	@NotNull
	private String name;
	
	@NotNull
	private String profile;
	
	@NotNull
	private String company;
	
	@NotNull
	private String title;

}
