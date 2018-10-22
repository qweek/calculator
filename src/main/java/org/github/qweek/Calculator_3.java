package org.github.qweek;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.StreamSupport.stream;

public class Calculator_3 implements Calculator {
    private static final Splitter SPLITTER = Splitter.on(CharMatcher.anyOf(",\n"));

    /**
     * 3. Allow the add method to handle new lines between numbers (instead of commas).
     */
    @Override
    public int add(String numbers) {
        if (Strings.isNullOrEmpty(numbers)) {
            return 0;
        }

        Stream<String> tokens = stream(SPLITTER.split(numbers).spliterator(), false);
        return sum(tokens.mapToInt(this::convert));
    }

    private int convert(String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Invalid number: '%s'", number));
        }
    }
}
