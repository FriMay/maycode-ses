package ru.maycode.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maycode.store.entities.SendEmailTaskEntity;

public interface SendEmailTaskRepository extends JpaRepository<SendEmailTaskEntity, Long> {
}
