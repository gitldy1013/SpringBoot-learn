package com.cmcc.demo.demo.controller;

import com.cmcc.demo.demo.entity.Person;
import com.cmcc.demo.demo.service.HelloService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//@Controller
@RestController
public class HelloController {

    @Resource
    private HelloService helloService;

    @Resource
    private Person person;

    @Resource
    JdbcTemplate jdbcTemplate;


    @ResponseBody
    @GetMapping("/query")
    public Map<String,Object> map(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * FROM department");
        return list.get(0);
    }

//    @ResponseBody
//    @RequestMapping("/hello")
    @GetMapping("/hello/{id}")
    public String hello(@PathVariable("id")String id){
        return "Hello World!"+ id;
    }

//    @ResponseBody
//    @RequestMapping("/person")
    @PostMapping("/person")
    public String personInfo(@RequestParam("name")String name){
        return name +"_"+ helloService.helloEvery(person);
    }
}
