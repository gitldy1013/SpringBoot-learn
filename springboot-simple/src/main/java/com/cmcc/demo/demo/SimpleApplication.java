package com.cmcc.demo.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用
 */
//使用MapperScan批量扫描所有的Mapper接口；
@EnableAsync //开启异步任务支持
@EnableScheduling //开启基于注解得定时任务支持
@MapperScan(value = "com.cmcc.demo.demo.mapper")
@SpringBootApplication
//@EnableCaching //开启缓存支持
//@EnableRabbit //开启RabbitMq支持
public class SimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
    }

}
