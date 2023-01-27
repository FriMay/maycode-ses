package ru.maycode.ses.worker.scheduled;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.maycode.ses.worker.service.EmailClientApi;
import ru.maycode.ses.worker.service.RedisLock;
import ru.maycode.ses.worker.service.RedisLockWrapper;
import ru.maycode.store.dao.SendEmailTaskDao;
import ru.maycode.store.entities.SendEmailTaskEntity;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskScheduler {

    SendEmailTaskDao sendEmailTaskDao;

    EmailClientApi emailClientApi;

    RedisLockWrapper redisLockWrapper;

    private static final String SEND_EMAIL_TASK_KEY_FORMAT = "maycode:send:email:task:%s";

    @Scheduled(cron = "*/5 * * * * *")
    public void executeSendEmailTasks() {

        log.debug("Worker start execution.");

        List<Long> sendEmailTaskIds = sendEmailTaskDao.findNotProcessedIds();

        for (Long sendEmailTaskId: sendEmailTaskIds) {

            String sendEmailTaskKey = getSendEmailTaskKey(sendEmailTaskId);

            redisLockWrapper.lockAndExecuteTask(
                    sendEmailTaskKey,
                    Duration.ofSeconds(5),
                    () -> sendEmail(sendEmailTaskId)
            );
        }
    }

    private void sendEmail(Long sendEmailTaskId) {

        Optional<SendEmailTaskEntity> optionalSendEmailTask = sendEmailTaskDao
                .findNotProcessedById(sendEmailTaskId);

        if (optionalSendEmailTask.isEmpty()) {
            log.info("Task %d already processed.".formatted(sendEmailTaskId));
            return;
        }

        SendEmailTaskEntity sendEmailTask = optionalSendEmailTask.get();

        String destinationEmail = sendEmailTask.getDestinationEmail();
        String message = sendEmailTask.getMessage();

        boolean delivered = emailClientApi.sendEmail(destinationEmail, message);

        if (delivered) {

            log.debug("Task %d processed.".formatted(sendEmailTask.getId()));
            sendEmailTaskDao.markAsProcessed(sendEmailTask);

            return;
        }

        log.warn("Task %d returned to process.".formatted(sendEmailTask.getId()));
        sendEmailTaskDao.updateLatestTryAt(sendEmailTask);
    }

    private static String getSendEmailTaskKey(Long taskId) {
        return SEND_EMAIL_TASK_KEY_FORMAT.formatted(taskId);
    }
}
