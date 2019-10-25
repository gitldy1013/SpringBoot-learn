package com.cmcc.starter.autoconfigurer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Resource;

@Data
@ConfigurationProperties(prefix = "cmcc.hello")
public class CmccHelloProperties {

    private String start = "start";
    private String end = "end";

}
