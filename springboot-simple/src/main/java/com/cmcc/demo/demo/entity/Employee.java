package com.cmcc.demo.demo.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Employee implements Serializable {
    private Integer id;
    private String lastName;
    private Integer gender;
    private String email;
    private Integer dId;
}
