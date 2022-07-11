package com.investingTech.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrackNetworthApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(TrackNetworthApplication.class, args);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
