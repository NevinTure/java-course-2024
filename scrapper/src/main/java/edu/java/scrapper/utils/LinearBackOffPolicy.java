package edu.java.scrapper.utils;

import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.StatelessBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.retry.support.RetrySynchronizationManager;

public class LinearBackOffPolicy extends StatelessBackOffPolicy {

    private final Sleeper sleeper = new ThreadWaitSleeper();
    private final Long backOffPeriod;

    public LinearBackOffPolicy(Long backOffPeriod) {
        this.backOffPeriod = backOffPeriod;
    }

    @Override
    protected void doBackOff() throws BackOffInterruptedException {
        try {
            int retry = RetrySynchronizationManager.getContext().getRetryCount();
            this.sleeper.sleep(retry * backOffPeriod);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }
}
