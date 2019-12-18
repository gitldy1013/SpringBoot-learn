package com.cmcc.demo.demo;

import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
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
    private RedisTemplate redisTemplate; //k-v都是Object

    @Resource
    private RedisTemplate myRedisTemplate; //k-v都是Object 自定义

    @Resource
    private StringRedisTemplate stringRedisTemplate; //k-v都是String

    @Test
    void contextLoads() throws SQLException {
        log.info(person.toString());

        //默认com.zaxxer.hikari.HikariDataSource
        //Druid:com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper
        log.info(dataSource.getClass().toString());
        Connection connection = dataSource.getConnection();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * FROM department");
        if (list.size() > 0) {
            log.info(list.get(0).toString());
        }
        connection.close();
    }

    /**
     * Redis常用的五种数据类型：
     * String(字符串),List(列表),Set(集合),Hash(哈希表),ZSet(有序集合)
     */
    @Test
    void redisTest() {
//        stringRedisTemplate.opsForValue();
//        stringRedisTemplate.opsForList();
//        stringRedisTemplate.opsForSet();
//        stringRedisTemplate.opsForHash();
//        stringRedisTemplate.opsForZSet();
        //操作字符串 其他类似
//        Integer append = stringRedisTemplate.opsForValue().append("msg", "hello");
//        String msg = stringRedisTemplate.opsForValue().get("msg");
//        log.info("msg"+msg);
        //保存对象
        redisTemplate.opsForValue().set("emp01",new Employee());
        Employee emp01 = (Employee) redisTemplate.opsForValue().get("emp01");
        log.info(emp01.toString());
        //序列化
        myRedisTemplate.opsForValue().set("emp02",new Employee());
        LinkedHashMap emp02 = (LinkedHashMap) myRedisTemplate.opsForValue().get("emp02");
        log.info(emp02.toString());
    }

}
