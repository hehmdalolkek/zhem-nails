package ru.zhem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZhemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhemServiceApplication.class, args);
    }
}
