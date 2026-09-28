package com.taskflow.task_service;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskServiceApplication {

	   public static void main(String[] args) {
	        Locale.setDefault(Locale.US);
	        SpringApplication.run(TaskServiceApplication.class, args);
	    }

}
