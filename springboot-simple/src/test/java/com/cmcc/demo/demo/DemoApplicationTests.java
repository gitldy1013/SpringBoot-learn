package com.cmcc.demo.demo;

import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
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
    private RedisTemplate<String, Employee> redisTemplate; //k-v都是Object

    @Resource
    private RedisTemplate<String, Employee> myRedisTemplate; //k-v都是Object 自定义

    @Resource
    private StringRedisTemplate stringRedisTemplate; //k-v都是String

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private AmqpAdmin amqpAdmin;

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
        Employee emp01 = redisTemplate.opsForValue().get("emp01");
        assert emp01 != null;
        log.info(emp01.toString());
        //序列化
        myRedisTemplate.opsForValue().set("emp02",new Employee());
        Employee emp02 = myRedisTemplate.opsForValue().get("emp02");
        assert emp02 != null;
        log.info(emp02.toString());
    }

    /**
     * 测试RabbitMq
     * 1.单播（点对点）
     * 2.广播
     */
    @Test
    void rabbitMqSendTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg","一个测试消息");
        map.put("data", Arrays.asList("myrabbitmq",123,true));
        //单播
        //rabbitTemplate.convertAndSend("exchange.direct","ldy.news",map);
        //广播
        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmail("123@123.com");
        employee.setGender(1);
        employee.setLastName("test");
        employee.setDId(1);
        rabbitTemplate.convertAndSend("exchange.fanout","",employee);
    }

    //@AfterEach
    public void rabbitMqReceiveTest(){
        Object receive = rabbitTemplate.receiveAndConvert("ldy.news");
        assert receive != null;
        log.info("msg {}",receive.getClass());
        log.info("msg {}",receive);
    }

    @Test
    public void rabbitAmqpTest(){
        amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
        log.info("amqpExchange: 创建完成");
        amqpAdmin.declareQueue(new Queue("amqpadmin.queue",true));
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue",Binding.DestinationType.QUEUE,"amqpadmin.exchange","amqp.haha",null));
    }
}
