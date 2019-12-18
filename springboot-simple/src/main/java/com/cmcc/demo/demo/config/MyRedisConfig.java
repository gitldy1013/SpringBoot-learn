package com.cmcc.demo.demo.config;

import com.cmcc.demo.demo.entity.Department;
import com.cmcc.demo.demo.entity.Employee;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "cache")
@Data
public class MyRedisConfig {

    public Long ttl;

    /**
     * 默认通过org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration创建redisTemplate
     * 这里自己指定自定义的myRedisTemplate来实现redis json序列化存取对象
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> myRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        //修改默认序列化方式为json
        template.setDefaultSerializer(serializer);
        return template;
    }

    /**
     * 对比org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     * 自定义CacheManager 这里Jackson2JsonRedisSerializer泛型定义需要指定 此处设计不是很好
     */
    @Primary //将同级Bean管理器指定默认值
    @Bean
    public CacheManager empRedisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(ttl))
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                                Jackson2JsonRedisSerializer<Employee>(Employee.class)));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }

    @Bean
    public CacheManager depRedisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(ttl))
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                                Jackson2JsonRedisSerializer<Department>(Department.class)));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }

}
