package ru.maycode.ses.worker.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class RedisLockWrapper {

    RedisLock redisLock;

    public void lockAndExecuteTask(String key, Duration duration, Runnable runnable) {

        try {

            if (!redisLock.acquireLock(key, duration)) {
                return;
            }

            runnable.run();

        } finally {
            redisLock.releaseLock(key);
        }
    }
}
