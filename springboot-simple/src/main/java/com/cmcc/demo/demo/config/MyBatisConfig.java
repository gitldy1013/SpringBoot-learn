package com.cmcc.demo.demo.config;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * 自动驼峰命名转换 (配置文件中也可以配置)
 */
//@org.springframework.context.annotation.Configuration
public class MyBatisConfig {

    //@Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true);
        };
    }
}
