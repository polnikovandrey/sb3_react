package com.mcfly.poll;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;

import java.util.TimeZone;

@SpringBootApplication
// Register JPA 2.1 converters: convert Java 8 Date/Time fields to SQL upon persisting.
@EntityScan(basePackageClasses = {PollApplication.class, Jsr310Converters.class})
public class PollApplication {

    public static void main(String[] args) {
        SpringApplication.run(PollApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // Default timezone = UTC
    }
}
