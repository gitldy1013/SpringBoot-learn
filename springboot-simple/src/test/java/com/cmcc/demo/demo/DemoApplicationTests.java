package com.cmcc.demo.demo;

//import com.cmcc.demo.demo.dao.EmpRepository;
import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.entity.Person;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
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
        redisTemplate.opsForValue().set("emp01", new Employee());
        Employee emp01 = redisTemplate.opsForValue().get("emp01");
        assert emp01 != null;
        log.info(emp01.toString());
        //序列化
        myRedisTemplate.opsForValue().set("emp02", new Employee());
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
    void rabbitMqSendTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "一个测试消息");
        map.put("data", Arrays.asList("myrabbitmq", 123, true));
        //单播
        //rabbitTemplate.convertAndSend("exchange.direct","ldy.news",map);
        //广播
        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmail("123@123.com");
        employee.setGender(1);
        employee.setLastName("test");
        employee.setDId(1);
        rabbitTemplate.convertAndSend("exchange.fanout", "", employee);
    }

    //@AfterEach
    public void rabbitMqReceiveTest() {
        Object receive = rabbitTemplate.receiveAndConvert("ldy.news");
        assert receive != null;
        log.info("msg {}", receive.getClass());
        log.info("msg {}", receive);
    }

    @Test
    public void rabbitAmqpTest() {
        amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
        log.info("amqpExchange: 创建完成");
        amqpAdmin.declareQueue(new Queue("amqpadmin.queue", true));
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE, "amqpadmin.exchange", "amqp.haha", null));
    }

    @Resource
    private JestClient jestClient;

    @Test
    public void JestIndexTest() {
        //向ES中索引（保存）数据
        Employee employee = new Employee();
        employee.setId(2);
        employee.setLastName("123");
        employee.setGender(1);
        employee.setEmail("123@test.com");
        employee.setDId(1);
        Index build = new Index.Builder(employee).index("ldy").type("news").id(employee.getId().toString()).build();
        try {
            jestClient.execute(build);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void JestSearchTest() throws IOException {
        //从ES中搜索（检索）数据
        String json = "{" +
                "    \"query\":{\n" +
                "        \"match\":{\n" +
                "            \"id\" : \"2\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Search build = new Search.Builder(json).addIndex("ldy").addType("news").build();
        SearchResult execute = jestClient.execute(build);
        String jsonString = execute.getJsonString();
        log.info("结果= {}", jsonString);
    }

    //@Resource
    //private EmpRepository empRepository;

    //@Test
    public void EsTest() {
        Employee employee = new Employee();
        employee.setId(2);
        employee.setLastName("321");
        employee.setGender(0);
        employee.setEmail("123");
        employee.setDId(1);
        //empRepository.index(employee);
        //Employee byLastNameLike = empRepository.findByLastNameLike("3");
        //log.info("结果：{}", byLastNameLike);
    }

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Test
    public void sendMail() throws MessagingException {
        //简单邮件
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject("通知");
        msg.setText("没事");
        msg.setTo("m13691363167@163.com");
        msg.setFrom("1126176532@qq.com");
        //javaMailSender.send(msg);
        //复杂邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setSubject("通知");
        mimeMessageHelper.setText("<b style='color:red'>没事</b>",true);
        mimeMessageHelper.setTo("m13691363167@163.com");
        mimeMessageHelper.setFrom("1126176532@qq.com");
        mimeMessageHelper.addAttachment("1.jpg",new File("C:\\Users\\liudongyang\\IdeaProjects\\demo\\images\\2018-02-04_123955.png"));
        mimeMessageHelper.addAttachment("2.jpg",new File("C:\\Users\\liudongyang\\IdeaProjects\\demo\\images\\rabbitmq-01.png"));
        javaMailSender.send(mimeMessage);
    }
}
