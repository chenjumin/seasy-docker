package com.seasy.docker.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main{
	public static void main(String[] args){
		SpringApplication springApp = new SpringApplication(Main.class);
		springApp.run(args);
	}
}
