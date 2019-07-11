package com.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName TestRibbonServer
 * @Description: 服务端启动类
 * @Author 小松
 * @Date 2019/7/9
 **/
@SpringBootApplication
@EnableEurekaClient
public class TestRibbonServer {
    public static void main(String[] args) {
        SpringApplication.run(TestRibbonServer.class,args);
    }
}