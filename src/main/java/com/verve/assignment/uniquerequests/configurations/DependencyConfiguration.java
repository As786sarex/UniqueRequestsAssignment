package com.verve.assignment.uniquerequests.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DependencyConfiguration {

    public static final String REDIS_HOST_KEY = "SPRING_REDIS_HOST";
    public static final String REDIS_PORT_KEY = "SPRING_REDIS_PORT";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Standalone Redis configuration (host, port, and optional password)
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(System.getenv(REDIS_HOST_KEY));
        redisConfig.setPort(Integer.parseInt(System.getenv(REDIS_PORT_KEY)));
        redisConfig.setPassword(""); // Leave empty if no password is set

        // Create Lettuce connection factory with the configuration
        return new LettuceConnectionFactory(redisConfig);
    }


    @Bean
    public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();

        // Set Redis connection factory
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Set key serializer (String)
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Set value serializer (Integer as String)
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));

        // Optional: set serializers for hash keys and values if needed
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
