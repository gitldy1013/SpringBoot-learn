package com.cmcc.starter.autoconfigurer.service.impl;

import com.cmcc.starter.autoconfigurer.CmccHelloProperties;
import com.cmcc.starter.autoconfigurer.service.CmccHelloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnWebApplication //web项目才会生效
@EnableConfigurationProperties(CmccHelloProperties.class)
public class CmccHelloServiceImpl implements CmccHelloService {

    @Resource
    CmccHelloProperties cmccHelloProperties;

    @Override
    public String sayHelloCmcc(String args) {
        return cmccHelloProperties.getStart()+args+cmccHelloProperties.getEnd();
    }
}
