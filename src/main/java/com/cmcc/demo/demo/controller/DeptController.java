package com.cmcc.demo.demo.controller;

import com.cmcc.demo.demo.entity.Department;
import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.mapper.DepartmentMapper;
import com.cmcc.demo.demo.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;


    @GetMapping("/dept/{id}")
    public Department getDepartment(@PathVariable("id") Integer id) {
        return departmentMapper.getDeptById(id);
    }

    @GetMapping("/dept")
    public Department insertDept(Department department) {
        departmentMapper.insertDept(department);
        return department;
    }

    @GetMapping("/emp/{id}")
    public Employee getEmp(@PathVariable("id") Integer id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setLastName("");
        employee.setGender(0);
        employee.setEmail("");
        employee.setDId(0);
        employeeMapper.insertEmp(employee);
        return employeeMapper.getEmpById(id);
    }


}
