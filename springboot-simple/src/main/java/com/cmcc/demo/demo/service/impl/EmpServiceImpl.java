package com.cmcc.demo.demo.service.impl;

import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmpServiceImpl implements EmpService {
    @Override
    //@RabbitListener(queues = "ldy.news")
    public void receive(Employee employee) {
        log.info("接收到EMP {}",employee);
    }

    //@RabbitListener(queues = "liudongyang.news")
    public void receiveMsg(Message msg) {
        log.info("接收到EMPMSG {}",msg.getBody());
    }
}
