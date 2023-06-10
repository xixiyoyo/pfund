package com.xiyo.pfund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@ComponentScan({"com.xiyo.common","com.xiyo.pfund"})
public class ServiceJobApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceJobApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
