package com.cmcc.demo.demo.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Employee {
    private Integer id;
    private String lastName;
    private Integer gender;
    private String email;
    private Integer dId;
}
