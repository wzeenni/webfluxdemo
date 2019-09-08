package com.webfluxdemo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	
	@Id
	private String id;
	
	@NotNull
	private Date date;
	
	@NotNull
	private Double lat;
	
	@NotNull
	private Double lng;
	
	@NotNull
	private String type;
	
	@NotNull
	private User owner;

}
