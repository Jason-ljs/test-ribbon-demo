package com.ribbon.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestServerController
 * @Description: 服务端控制层接口，让客户端调用此接口
 * @Author 小松
 * @Date 2019/7/9
 **/
@RestController
public class TestServerController {

    //调用配置中的端口
    @Value("${server.port}")
    private String port;

    @RequestMapping("testServer")
    public String testServer(){
        //为了让我们看到调用的是哪个端口的服务
        System.out.println("========server "+port+"===========");
        return port;
    }
}