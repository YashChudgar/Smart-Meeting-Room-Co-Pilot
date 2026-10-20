package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.entity.Room;
import com.example.demo.repository.RoomRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
// remember to take out once postgresql is up
	@Bean
    public CommandLineRunner demoData(RoomRepository roomRepository) {
        return args -> {
            Room room = new Room();
            room.setName("Main Conference Room");
            room.setCapacity(10);
            room.setLocation("Building A");
            roomRepository.save(room);
        };
    }

}
