package com.cmcc.demo.demo.dao;

import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

//继承ElasticsearchRepository来完成对数据的索引/检索操作 泛型：<实体类型，主键类型>
public interface EmpRepository extends ElasticsearchRepository<Employee,Integer> {

    Employee findByLastNameLike(String lastName);

}
