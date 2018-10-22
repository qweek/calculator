package org.github.qweek;

import static java.lang.String.format;
import static java.util.stream.IntStream.of;

public class Calculator_1 implements Calculator {
    /**
     * 1. Create a simple string calculator with a method int add(String numbers)
     *   a. The method can take 0, 1 or 2 numbers and will return their sum (for an empty string it will return 0) for example “” or “1” or “1,2”
     *   b. Start with the simplest test case of an empty string and move to 1 and 2 numbers.
     */
    @Override
    public int add(String numbers) {
        if (numbers == null) {
            return 0;
        }

        int index = numbers.indexOf(",");
        if (index < 0) {
            return sum(of(convert(numbers)));
        }

        String first = numbers.substring(0, index);
        String second = numbers.substring(index + 1);
        return sum(of(convert(first), convert(second)));
    }

    private int convert(String number) {
        try {
            return number.isEmpty() ? 0 : Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Invalid number: '%s'", number));
        }
    }
}
