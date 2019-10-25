package com.cmcc.demo.demo.service.impl;

import com.cmcc.demo.demo.entity.Person;
import com.cmcc.demo.demo.service.HelloService;
import com.cmcc.starter.autoconfigurer.service.CmccHelloService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HelloServiceImpl implements HelloService {


    @Resource
    CmccHelloService cmccHelloService;

    @Override
    public String cmccSayHello(String args) {
        args = "_" + args + "_";
        return cmccHelloService.sayHelloCmcc(args);
    }

    @Override
    public String helloEvery(Person person) {
        String lastName = person.getLastName();
        return lastName;
    }

}
