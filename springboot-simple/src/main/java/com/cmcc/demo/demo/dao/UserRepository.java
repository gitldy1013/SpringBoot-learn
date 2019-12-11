package com.cmcc.demo.demo.dao;

import com.cmcc.demo.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//继承JpaRepository来完成对数据库的操作 泛型：<实体类型，主键类型>
public interface UserRepository extends JpaRepository<User,Integer> {
}
