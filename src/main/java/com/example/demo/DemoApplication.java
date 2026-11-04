package com.example.demo;

import java.time.LocalDateTime;

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
CommandLineRunner initDatabase(RoomRepository roomRepository) {
    return args -> {
        // Only seed if the table is empty
        if (roomRepository.count() == 0) {
            Room room1 = new Room();
            room1.setName("Room A");
            room1.setRoomNumber("101");
            room1.setCapacity(6);
            room1.setLocation("Building 1");
            room1.setAvailable(true);
            room1.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room1);

            Room room2 = new Room();
            room2.setName("Room B");
            room2.setRoomNumber("102");
            room2.setCapacity(10);
            room2.setLocation("Building 1");
            room2.setAvailable(false);
            room2.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room2);

            Room room3 = new Room();
            room3.setName("Room C");
            room3.setRoomNumber("103");
            room3.setCapacity(4);
            room3.setLocation("Building 2");
            room3.setAvailable(true);
            room3.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room3);

            Room room4 = new Room();
            room4.setName("Room D");
            room4.setRoomNumber("104");
            room4.setCapacity(12);
            room4.setLocation("Building 2");
            room4.setAvailable(true);
            room4.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room4);

            Room room5 = new Room();
            room5.setName("Conference Suite 1");
            room5.setRoomNumber("201");
            room5.setCapacity(20);
            room5.setLocation("Main Building");
            room5.setAvailable(false);
            room5.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room5);
        }
    };
}

}
