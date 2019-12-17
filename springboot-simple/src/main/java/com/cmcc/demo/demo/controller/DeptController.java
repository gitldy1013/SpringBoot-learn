package com.cmcc.demo.demo.controller;

import com.cmcc.demo.demo.entity.Department;
import com.cmcc.demo.demo.entity.Employee;
import com.cmcc.demo.demo.mapper.DepartmentMapper;
import com.cmcc.demo.demo.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CacheConfig(cacheNames = "emp")
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
     *        8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration"【默认】
     *        9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
     *      默认缓存配置类：
     *          org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration:
     *          向容器中注册了一个缓存管理器CacheManager: org.springframework.cache.concurrent.ConcurrentMapCacheManager
     *          这个CacheManager可以创建和获取ConcurrentMapCache类型的缓存组件，可以将数据保存再ConcurrentMap中
     *
     * 运行流程：
     *      @Cacheable:
     *      1.目标方法运行之前通过CacheManager按照指定的cacheMames获取缓存组件（Cache）
     *      首次判断为空后需要创建缓存
     *      2.通过策略生成得key去Cache中查找缓存 默认key为参数
     *      通过默认得KeyGenerator（org.springframework.cache.interceptor.SimpleKeyGenerator）生成
     *          1.如果没有参数会使用new SimpleKey()作为key
     *          2.如果为一个参数会使用这个参数作为key
     *          3.如果为多个参数则会使用new SimpleKey(params)作为key
     *      3.当没有查询到缓存时会调用目标方法。并将结果放入缓存中
     *      总结：
     *      通过@Cacheable标注的方法在执行之前会先去缓存中通过参数的值检查缓存
     *      如果没有查询到缓存会执行目标方法并将返回的非空结果放入缓存
     *      下次调用目标方法时就可以直接使用缓存下来的数据并不会执行目标方法
     *      @CachePut:
     *      1.即调用方法，有更新缓存 先调用方法 再放入缓存
     *      2.同步更新缓存
     *      @CacheEvict:
     *      1.缓存清除
     *      2.通过value指定缓存 通过指定具体的key删除指定要删除的缓存 也可以通过allEntries = true删除所有指定cache中的缓存数据
     *      3.如果需要在指定目标方法之前执行可以通过指定beforeInvocation = true实现预先删除指定缓存（避免因为目标方法执行异常无法清除缓存数据）
     *      @Caching:
     *      1.组合注解 可以在复制的缓存策略时使用 定义复杂的缓存规则
     *      #以上为方法级别注解#
     *      @CacheConfig:
     *      1.类级别缓存注解
     *
     * 核心组件：
     *      1.通过CacheManager（ConcurrentMapCacheManager）按照名字获取到Cache（ConcurrentMapCache）组件
     *      2.key通过使用KeyGenerator（SimpleKeyGenerator）生成
     *
     * Cacheable属性:
     *      cacheNames/value: 指定缓存组件的名字 可以指定多个名字 数据会缓存到这些Cache中
     *      key：缓存数据时使用的key（键值对的方式）默认是方法的参数的值 （支持SpEL表达式）
     *      keyGenerator: key的生成器，可以自己指定key的生成器的组件id
     *      key/keyGenerator: 同时只能使用一个
     *      cacheManager/cacheResolver: 指定缓存管理器或者指定缓存解析器
     *      conditon: 指定符合条件情况下才缓存
     *      unless: 指定条件情况下不缓存
     *      sync: 指定是否使用异步模式
     *
     * 缓存中间件：redis,memcached,ehcache
     *
     * 整合Redis作为缓存：
     *      1.基于docker的方式安装redis
     *      2.引入redis对应starter依赖spring-boot-starter-data-redis
     *      3.配置文件引入redis配置 主要是端口地址和访问密码
     * @param id
     * @return
     */
    @GetMapping("/dept/{id}")
    @Cacheable(cacheNames = {"dep","t1"},key = "#root.method+'['+#id+']'", condition = "#id>0", unless = "#result == null")
    public Department getDepartment(@PathVariable("id") Integer id) {
        return departmentMapper.getDeptById(id);
    }

    @CachePut(value = "dept")
    @GetMapping("/dept")
    public Department insertDept(Department department) {
        departmentMapper.insertDept(department);
        return department;
    }

    @Cacheable(value = "emp",keyGenerator = "myKeyGenerator",condition = "#a0>1 and #id<100",unless = "#a0==2")
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

    @CacheEvict(value = "emp",key = "#id",allEntries = true,beforeInvocation = true)
    @GetMapping(value = "/emp/del/{id}")
    public void delEmp(@PathVariable("id") Integer id){
        //TODO Del Emp
    }

    @Caching(
            cacheable = {
                @Cacheable(value = "emp",key = "#department.id")
            },
            put = {
                @CachePut(value = "emp",key = "#result.id"),
                @CachePut(value = "emp",key = "#result.lastName")
            }
    )
    public Employee getEmpByDept(Department department){
        // TODO GetEmpByDept
        return new Employee();
    }

}
