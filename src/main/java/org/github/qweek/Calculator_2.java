package org.github.qweek;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class Calculator_2 implements Calculator {
    /**
     * 2. Allow the add method to handle an unknown amount of numbers.
     */
    @Override
    public int add(String numbers) {
        if (numbers == null) {
            return 0;
        }

        String[] tokens = numbers.split(",");
        if (tokens.length == 0) {
            return 0;
        }

        return sum(stream(tokens).mapToInt(this::convert));
    }

    private int convert(String number) {
        try {
            return number.isEmpty() ? 0 : Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Invalid number: '%s'", number));
        }
    }
}
