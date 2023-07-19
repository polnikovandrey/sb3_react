package com.mcfly.poll;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
// Register JPA 2.1 converters: convert Java 8 Date/Time fields to SQL upon persisting.
//@EntityScan(basePackageClasses = {Sb3Application.class, Jsr310Converters.class})      TODO check wo
public class Sb3Application {

    public static void main(String[] args) {
        SpringApplication.run(Sb3Application.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // Default timezone = UTC
    }
}
