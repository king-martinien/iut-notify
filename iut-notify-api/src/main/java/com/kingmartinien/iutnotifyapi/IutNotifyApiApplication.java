package com.kingmartinien.iutnotifyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class IutNotifyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IutNotifyApiApplication.class, args);
    }

}
