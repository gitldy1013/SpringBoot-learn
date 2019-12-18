package com.cmcc.demo.demo.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Department implements Serializable {
    private String id;
    private String departmentName;
}
