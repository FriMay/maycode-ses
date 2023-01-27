package ru.maycode.ses.worker.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.maycode.store.EnableSesStore;

@Import({
        EnableSesStore.class
})
@Configuration
public class ImportConfig {
}
