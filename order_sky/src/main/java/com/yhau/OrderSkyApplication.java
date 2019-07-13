package com.yhau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderSkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSkyApplication.class, args);
    }

}
