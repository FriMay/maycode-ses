package ru.maycode.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@ComponentScan("ru.maycode.store.dao")
@EntityScan("ru.maycode.store.entities")
@EnableJpaRepositories("ru.maycode.store.repositories")
public class EnableSesStore {
}
