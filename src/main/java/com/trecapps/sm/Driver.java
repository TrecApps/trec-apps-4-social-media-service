package com.trecapps.sm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan({
        "com.trecapps.sm.common.*",                     // Scan this app
        "com.trecapps.auth.common.*",               // Authentication library
        "com.trecapps.auth.webflux.*",
        "${trecapps.sm.mode}"
})
@EnableMongoRepositories("com.trecapps.subscription.repos")
@EnableWebFlux
public class Driver {
    public static void main(String[] args) {
        SpringApplication.run(Driver.class, args);
    }
}
