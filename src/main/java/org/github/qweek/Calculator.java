package org.github.qweek;

import java.util.stream.IntStream;

public interface Calculator {
    /**
     * Create a simple string calculator with a method int add(String numbers)
     */
    int add(String numbers);

    default int sum(IntStream numbers) {
        return numbers == null ? 0 : numbers.reduce(Math::addExact).orElse(0);
    }
}
