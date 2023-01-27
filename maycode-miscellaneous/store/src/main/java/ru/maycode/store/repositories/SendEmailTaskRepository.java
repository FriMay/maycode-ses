package ru.maycode.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.maycode.store.entities.SendEmailTaskEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SendEmailTaskRepository extends JpaRepository<SendEmailTaskEntity, Long> {

    @Query("""
        SELECT task.id
        FROM SendEmailTaskEntity task
        WHERE task.processedAt IS NULL
            AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtLte)
        ORDER BY task.createdAt
    """)
    List<Long> findAllNotProcessed(Instant latestTryAtLte);

    @Query("""
        SELECT task
        FROM SendEmailTaskEntity task
        WHERE task.id = :id
            AND task.processedAt IS NULL
            AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtLte)
    """)
    Optional<SendEmailTaskEntity> findNotProcessedById(Long id, Instant latestTryAtLte);

    @Modifying
    @Query("""
        UPDATE SendEmailTaskEntity task
        SET task.processedAt = NOW()
        WHERE task.id = :id
    """)
    void markAsProcessed(Long id);

    @Modifying
    @Query("""
        UPDATE SendEmailTaskEntity task
        SET task.latestTryAt = NOW()
        WHERE task.id = :id
    """)
    void updateLatestTryAt(Long id);

}
