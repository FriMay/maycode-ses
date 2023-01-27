package ru.maycode.ses.worker.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class RedisLock {

    private static final String LOCK_FORMAT = "%s::lock";

    ValueOperations<String, Long> valueOperations;

    RedisTemplate<String, Long> redisTemplate;

    public boolean acquireLock(String key, Duration duration) {

        String lockKey = getLockKey(key);

        Long expiresAtMillis = valueOperations.get(lockKey);

        long currentTimeMillis = System.currentTimeMillis();

        if (Objects.nonNull(expiresAtMillis)) {

            if (currentTimeMillis <= expiresAtMillis) {
                return false;
            }

            redisTemplate.delete(lockKey);
        }

        return Optional
                .ofNullable(
                        valueOperations.setIfAbsent(
                                lockKey,
                                currentTimeMillis + duration.toMillis()
                        )
                )
                .orElse(false);
    }

    public void releaseLock(String key) {

        String lockKey = getLockKey(key);

        redisTemplate.delete(lockKey);
    }

    private static String getLockKey(String key) {
        return LOCK_FORMAT.formatted(key);
    }
}
