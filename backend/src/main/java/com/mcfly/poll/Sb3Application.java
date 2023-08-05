package com.mcfly.poll;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class Sb3Application {

    public static void main(String[] args) {
        SpringApplication.run(Sb3Application.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // Default timezone = UTC
    }
}
