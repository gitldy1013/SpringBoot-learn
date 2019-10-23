package com.cmcc.demo.demo;

import com.cmcc.demo.demo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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

}
