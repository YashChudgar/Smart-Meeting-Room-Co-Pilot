package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController 
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public String health() {
        return "Smart Meeting Room backend is running";
    }
    
}
