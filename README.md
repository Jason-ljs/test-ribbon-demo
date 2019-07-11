# Ribbon 实现负载均衡集群搭建

> Spring Cloud Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，主要配置在客户端
>
> 想深入了解Ribbon的推荐阅读：<https://www.jianshu.com/p/1bd66db5dc46>  

1. 创建一个 spring boot 的 maven 项目(声明 spring cloud 项目并规定了各组件的版本) 

   引入pom文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com.ribbon</groupId>
       <artifactId>test-ribbon-demo</artifactId>
       <packaging>pom</packaging>
       <version>1.0-SNAPSHOT</version>
       <modules>
           <module>demo-eureka-server</module>
           <module>demo-ribbon-server</module>
           <module>demo-ribbon-client</module>
       </modules>
   
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.0.3.RELEASE</version>
       </parent>
   
       <dependencyManagement>
           <dependencies>
               <dependency>
                   <groupId>org.springframework.cloud</groupId>
                   <artifactId>spring-cloud-dependencies</artifactId>
                   <version>Finchley.SR2</version>
                   <scope>import</scope>
                   <type>pom</type>
               </dependency>
           </dependencies>
       </dependencyManagement>
   
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

2. 因为所有的服务都是基于注册中心的，所以需要创建一个注册中心(我们此次用Eureka)

   引入pom文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>test-ribbon-demo</artifactId>
           <groupId>com.ribbon</groupId>
           <version>1.0-SNAPSHOT</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
   
       <artifactId>demo-eureka-server</artifactId>
   
       <dependencies>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

   配置文件

   ```perl
   #服务的端口号
   server.port=8080
   #服务的名字
   spring.application.name=TEST-EUREKA-SERVER
   
   #配置数据复制的peer节点
   eureka.client.service-url.defaultZone= http://localhost:8080/eureka
   #关闭自我保护
   eureka.server.enable-self-preservation=false
   #不注册自己到Eureka注册中心
   eureka.client.register-with-eureka=true
   #配置不获取注册信息
   eureka.client.fetch-registry=false
   ```

   创建启动类 

   ```java
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
   
   ```

3. 创建服务端项目

   引入pom文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>test-ribbon-demo</artifactId>
           <groupId>com.ribbon</groupId>
           <version>1.0-SNAPSHOT</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
   
       <artifactId>demo-ribbon-server</artifactId>
   
       <dependencies>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

   配置文件

   ```perl
   #服务的端口
   server.port=8082
   #服务名称
   spring.application.name=SERVER
   #注册中心的地址
   eureka.client.service-url.defaultZone= http://localhost:8080/eureka
   ```

   启动类

   ```java
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
   
   ```

   创建服务端控制层接口，让客户端调用此接口

   ```java
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
   ```

4. 因为是负载均衡集群，所以需要创建多个服务，其实和上面这个服务创建的是一样的，只是端口号不一样，为了方便我们测试，可以这样干(开发阶段可以，生产阶段是不可以的)：

   1. 先将此服务启动
   2. 更改配置文件的端口号
   3. 再此启动此服务

5. 经过上个操作我们就开启了多个服务，接下来我们创建客户端

   引入pom文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>test-ribbon-demo</artifactId>
           <groupId>com.ribbon</groupId>
           <version>1.0-SNAPSHOT</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
   
       <artifactId>demo-ribbon-client</artifactId>
   
       <dependencies>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

   配置文件

   ```perl
   #客户端的端口号
   server.port=8085
   #客户端的服务名称
   spring.application.name=CLIENT
   #注册中心地址
   eureka.client.service-url.defaultZone= http://localhost:8080/eureka
   #为单个服务配置负载均衡策略
   #SERVER.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
   
   #配置饥饿加载
   #ribbon.eager-load.enabled=true
   #ribbon.eager-load.clients=client
   ```

   启动类

   ```java
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
   ```

   配置类(此类是声明RestTemplate，也可以配置负载均衡策略是全局的)

   ```java
   package com.ribbon.config;
   
   import com.netflix.loadbalancer.IRule;
   import com.netflix.loadbalancer.RandomRule;
   import org.springframework.cloud.client.loadbalancer.LoadBalanced;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.web.client.RestTemplate;
   
   /**
    * @ClassName MyConfig
    * @Description: TODO
    * @Author 小松
    * @Date 2019/7/9
    **/
   @Configuration
   public class MyConfig {
   
       @Bean
       @LoadBalanced
       public RestTemplate getRestTemplate(){
           return new RestTemplate();
       }
   
       /**
        * 配置负载均衡策略
        * 默认为 轮询策略
        */
   
       //随机策略
       public IRule getIRule(){
           return new RandomRule();
       }
   
   }
   ```

   客户端控制层接口

   ```java
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
   
   ```

6. **到此负载均衡集群搭建完成**

项目源码：[https://blog.csdn.net/weixin_43650254/article/details/95471565 ](https://blog.csdn.net/weixin_43650254/article/details/95471565)
