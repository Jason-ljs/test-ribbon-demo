package com.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName TestEureka
 * @Description: 注册中心启动类
 * @Author 小松
 * @Date 2019/7/9
 **/
@SpringBootApplication
@EnableEurekaServer
public class TestEureka {
    public static void main(String[] args) {
        SpringApplication.run(TestEureka.class, args);
    }
}
