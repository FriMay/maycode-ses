package ru.maycode.ses.worker.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class EmailClientApi {

    @SneakyThrows
    public void sendEmail(String destinationEmail, String message) {

        // pururururu, email send
        Thread.sleep(1000L);

        log.info("Email to %s successfully sent.".formatted(destinationEmail));
    }
}
