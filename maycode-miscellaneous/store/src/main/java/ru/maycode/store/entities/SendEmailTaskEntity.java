package ru.maycode.store.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@Entity
@Table(name = "send_email_task")
public class SendEmailTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Builder.Default
    @Column(updatable = false, nullable = false)
    Instant createdAt = Instant.now();

    Instant latestTryAt;

    Instant processedAt;

    String destinationEmail;

    String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendEmailTaskEntity that = (SendEmailTaskEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
