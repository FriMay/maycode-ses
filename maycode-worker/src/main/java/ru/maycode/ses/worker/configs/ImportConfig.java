package ru.maycode.ses.worker.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.maycode.store.EnableSesStore;

@Import({
        EnableSesStore.class
})
@Configuration
@EnableScheduling
public class ImportConfig {
}
