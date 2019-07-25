package com.yhau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserSkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserSkyApplication.class, args);
    }
}
