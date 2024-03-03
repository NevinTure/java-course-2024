package edu.java.scrapper.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class LinkUpdateScheduler {


    @Scheduled(fixedDelayString =
        "#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler().forceCheckDelay()}")
    public void update() {
        log.info("Проверка по расписанию");
    }
}
