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

@Document(collection="tweets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

	@Id
	private String id;
	
	private Integer userId;

	@NotBlank
	@Size(max = 140)
	private String text;

	@NotNull
	private Date createdAt = new Date();
	
	private Boolean active = false;

}
