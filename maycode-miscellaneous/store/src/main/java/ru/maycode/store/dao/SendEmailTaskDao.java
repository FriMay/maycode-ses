package ru.maycode.store.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.maycode.store.entities.SendEmailTaskEntity;
import ru.maycode.store.repositories.SendEmailTaskRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskDao {

    SendEmailTaskRepository sendEmailTaskRepository;

    @Transactional
    public SendEmailTaskEntity save(SendEmailTaskEntity entity) {
        return sendEmailTaskRepository.save(entity);
    }

    public List<Long> findNotProcessedIds() {
        return sendEmailTaskRepository.findAllNotProcessed();
    }

    @Transactional
    public void markAsProcessed(SendEmailTaskEntity entity) {
        sendEmailTaskRepository.markAsProcessed(entity.getId());
    }

    @Transactional
    public void updateLatestTryAt(SendEmailTaskEntity entity) {
        sendEmailTaskRepository.updateLatestTryAt(entity.getId());
    }

    public Optional<SendEmailTaskEntity> findNotProcessedById(Long sendEmailTaskId) {
        return sendEmailTaskRepository.findNotProcessedById(sendEmailTaskId);
    }
}
