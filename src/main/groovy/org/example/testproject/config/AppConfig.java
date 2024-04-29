package org.example.testproject.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "org.example.testproject")
@EnableFeignClients(basePackages = {"org.example.testproject.client"})
public class AppConfig {
}
