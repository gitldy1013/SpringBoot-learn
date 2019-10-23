package com.cmcc.demo.demo.service.impl;

import com.cmcc.demo.demo.entity.Person;
import com.cmcc.demo.demo.service.HelloService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String helloEvery(Person person) {
        String lastName = person.getLastName();
        return lastName;
    }
}
