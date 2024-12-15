package com.verve.assignment.uniquerequests.configurations;

import com.verve.assignment.uniquerequests.models.SendCountRequestModel;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DependencyConfiguration {

    public static final String REDIS_HOST_KEY = "SPRING_REDIS_HOST";
    public static final String REDIS_PORT_KEY = "SPRING_REDIS_PORT";
    public static final String KAFKA_BOOTSTRAP_SERVER_KEY = "SPRING_KAFKA_BOOTSTRAP_SERVERS";
    public static final String VERVE_COUNT_TOPIC_NAME_KEY = "VERVE_COUNT_TOPIC_NAME";

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
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(KAFKA_BOOTSTRAP_SERVER_KEY));
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(System.getenv(VERVE_COUNT_TOPIC_NAME_KEY), 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, SendCountRequestModel> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv(KAFKA_BOOTSTRAP_SERVER_KEY));
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SendCountRequestModel> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
