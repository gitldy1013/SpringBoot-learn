package com.cmcc.demo.demo.config;

import com.cmcc.demo.demo.entity.Person;
import com.cmcc.demo.demo.service.HelloPerson;
import com.cmcc.demo.demo.service.HelloService;
import com.cmcc.demo.demo.service.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Configuration：指明当前类是一个配置类；就是来替代之前的Spring配置文件
 *
 * 在配置文件中用<bean><bean/>标签添加组件
 *
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({Person.class})//坑：使用自定义配置文件并且没有讲配置信息引入到springboot的配置环境时 此注解无效
public class MyAppConfig {

    @Resource
    Person person;

    //将方法的返回值添加到容器中；容器中这个组件默认的id就是方法名
    @Bean
    public HelloPerson HelloPerson(){
        log.info(person.toString());
        log.info("配置类@Bean给容器中添加组件了...");
        return new HelloPerson();
    }
}