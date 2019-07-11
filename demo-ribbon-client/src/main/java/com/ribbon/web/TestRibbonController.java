package com.ribbon.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName TestRibbonController
 * @Description: 客户端控制层接口，供用户访问
 * @Author 小松
 * @Date 2019/7/9
 **/
@RestController
public class TestRibbonController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("testClient")
    public String testClient(){
        //Ribbon 负载均衡提供方式
        String forObject = restTemplate.getForObject("http://SERVER/testServer", String.class);
        return forObject;
    }
}