package com.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName TestRibonClient
 * @Description: 客户端启动类
 * @Author 小松
 * @Date 2019/7/9
 **/
@SpringBootApplication
@EnableEurekaClient
public class TestRibonClient {
    public static void main(String[] args) {
        SpringApplication.run(TestRibonClient.class,args);
    }
}