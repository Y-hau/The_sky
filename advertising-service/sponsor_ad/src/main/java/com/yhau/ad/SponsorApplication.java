package com.yhau.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
public class SponsorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SponsorApplication.class, args);
    }
}
