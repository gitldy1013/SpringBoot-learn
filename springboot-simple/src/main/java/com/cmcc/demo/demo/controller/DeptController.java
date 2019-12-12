package com.cmcc.demo.demo.controller;

import com.cmcc.demo.demo.entity.Department;
import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.mapper.DepartmentMapper;
import com.cmcc.demo.demo.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;


    /**
     * 查询部门信息：
     * 将方法运行返回的结果进行缓存
     * CahceManager是管理多个Cache组件的，对缓存的CRUD操作再Cache组件中，每个缓存组件有自己的唯一一个名字
     * Cacheable:
     *      cacheNames/value: 指定缓存组件的名字
     *      key：缓存数据时使用的key（键值对的方式）默认是方法的参数的值 （支持SpEL表达式）
     *      keyGenerator: key的生成器，可以自己指定key的生成器的组件id
     *      key/keyGenerator: 同时只能使用一个
     *      cacheManager/cacheResolver: 指定缓存管理器或者指定缓存解析器
     *      conditon: 指定符合条件情况下才缓存
     *      unless: 指定条件情况下不缓存
     *      sync: 指定是否使用异步模式
     * 工作原理：
     *      自动配置类：CacheAutoConfiguration
     *      缓存的配置类：
     *        0 = "org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration"
     *        1 = "org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration"
     *        2 = "org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration"
     *        3 = "org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration"
     *        4 = "org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration"
     *        5 = "org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration"
     *        6 = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration"
     *        7 = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration"
     *        8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration"
     *        9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
     *      默认缓存配置类：
     *          org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration:
     *          向容器中注册了一个缓存管理器CacheManager: org.springframework.cache.concurrent.ConcurrentMapCacheManager
     *          这个CacheManager可以创建和获取ConcurrentMapCache类型的缓存组件，可以将数据保存再ConcurrentMap中
     *
     * @param id
     * @return
     */
    @GetMapping("/dept/{id}")
    @Cacheable(cacheNames = {"emp","t1"},key = "#root.args[0]", condition = "#id>0", unless = "#result == null")
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
