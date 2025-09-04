package com.trecapps.sm;

import com.microsoft.applicationinsights.attach.ApplicationInsights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan({
        "com.trecapps.sm.common.*",                     // Scan this app

        "com.trecapps.sm.common",
        "com.trecapps.auth.common.*",               // Authentication library
        "com.trecapps.auth.webflux.*",
        "com.trecapps.sm.profile.*",                // Enable Profile Features
        "com.trecapps.sm.content"                   // Enable Content Features
})
@EnableReactiveMongoRepositories
@EnableWebFlux
public class MonoDriver {
    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(MonoDriver.class, args);
    }
}
