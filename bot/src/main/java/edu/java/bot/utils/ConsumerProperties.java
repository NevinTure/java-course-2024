package edu.java.bot.utils;

public record ConsumerProperties(
    String bootstrapServers,
    String groupId,
    String autoOffsetReset,
    Integer maxPollIntervalMs,
    Boolean enableAutoCommit,
    Integer concurrency
) {
}
