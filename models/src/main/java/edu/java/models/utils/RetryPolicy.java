package edu.java.models.utils;

import java.util.List;

public record RetryPolicy(Mode mode, List<Integer> codes) {
}
