package com.cmcc.demo.demo;

import com.cmcc.demo.demo.entity.Person;
import com.cmcc.demo.demo.utils.FileToBase64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class DemoApplicationTests {

    @Autowired
    Person person;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    private RestTemplate restTemplate;

    @Test
    void contextLoads() throws SQLException {
        log.info(person.toString());

        //默认com.zaxxer.hikari.HikariDataSource
        //Druid:com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper
        log.info(dataSource.getClass().toString());
        Connection connection = dataSource.getConnection();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * FROM department");
        log.info(list.get(0).toString());
        connection.close();
    }

    @Test
    void getToken() throws Exception {
        String client_id = "bI64wRMZ9nEEAAlROg3TPIqa";
        String client_secret = "ny5EYjweD8tB8wRGI5COifGA79zR2e9E";
        String forObject = restTemplate.postForObject("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret+"&grant_type=client_credentials",null, String.class);
        log.info(forObject);
    }

    @Test
    void getBusTicketInfo() throws Exception {
        //headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        //body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("image", FileToBase64.encodeBase64File("C:\\Users\\liudongyang\\Desktop\\test.jpg"));
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        String token = "24.b6d0efba81f6be34c97ea5bb9d80eece.2592000.1576058013.282335-17747458";
        //post
        String body = restTemplate.postForEntity("https://aip.baidubce.com/rest/2.0/ocr/v1/taxi_receipt?access_token="+token, requestEntity, String.class).getBody();
        log.info(body);
    }

}
