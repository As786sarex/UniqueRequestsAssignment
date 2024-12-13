package com.verve.assignment.uniquerequests.repositories.impl;

import com.verve.assignment.uniquerequests.repositories.VerveDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@Profile("extension")
public class RedisVerveDataRepository implements VerveDataRepository {

    public static final String COUNT_KEY = "count_key";

    private final RedisTemplate<String, Long> redisTemplate;

    public RedisVerveDataRepository(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean upsert(int id) {
        String key = "uniqueId:" + id;
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, 1L, Duration.ofMinutes(1));
        boolean isUnique = Boolean.TRUE.equals(isNew);
        if (isUnique) {
            redisTemplate.opsForValue().increment(COUNT_KEY);
        }
        return isUnique;
    }

    @Override
    public long getUniqueCount() {
        return Optional.ofNullable(redisTemplate.opsForValue().get(COUNT_KEY)).orElse(0L);
    }

    @Override
    public long fetchAndReset() {
        return Optional.ofNullable(redisTemplate.opsForValue().getAndSet(COUNT_KEY, 0L)).orElse(0L);
    }
}
