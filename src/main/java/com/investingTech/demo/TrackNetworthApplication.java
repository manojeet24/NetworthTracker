package com.investingTech.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
public class TrackNetworthApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(TrackNetworthApplication.class, args);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
