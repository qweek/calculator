package org.github.qweek;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.StreamSupport.stream;

public class Calculator_6 implements Calculator {
    private static final String DEFAULT_DELIMITER = ",";
    private static final Splitter DELIMITER_SPLITTER = Splitter.on('\n').limit(2);

    /**
     * 6. Numbers bigger than 1000 should be ignored, so adding 2 + 1001 = 2.
     */
    @Override
    public int add(String numbers) {
        if (Strings.isNullOrEmpty(numbers)) {
            return 0;
        }

        if (numbers.startsWith("//")) {
            List<String> tokens = DELIMITER_SPLITTER.splitToList(numbers.substring(2));
            String delimiter = tokens.get(0);
            if (delimiter.isEmpty()) {
                throw new IllegalArgumentException("Delimiter is empty");
            }
            if (delimiter.length() > 1) {
                throw new IllegalArgumentException("Delimiter too long");
            }
            if (tokens.size() == 1) {
                return 0;
            }
            return add(delimiter, tokens.get(1));
        }

        return add(DEFAULT_DELIMITER, numbers);
    }

    private int add(String delimiter, String numbers) {
        if (Strings.isNullOrEmpty(numbers)) {
            return 0;
        }
        Splitter splitter = Splitter.on(delimiter);
        Stream<String> tokens = stream(splitter.split(numbers).spliterator(), false);

        NegativeNotThousandFilter filter = new NegativeNotThousandFilter();
        int result = sum(tokens.mapToInt(this::convert).filter(filter));
        filter.throwIfNegatives();
        return result;
    }

    private int convert(String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Invalid number: '%s'", number));
        }
    }

    private class NegativeNotThousandFilter implements IntPredicate {
        private final List<String> negatives = new ArrayList<>();

        @Override
        public boolean test(int value) {
            boolean negative = value < 0;
            if (negative) {
                negatives.add(String.valueOf(value));
            }
            return !negative && value <= 1000;
        }

        void throwIfNegatives() {
            if (!negatives.isEmpty()) {
                throw new IllegalArgumentException(String.format("negatives not allowed: %s", join(",", negatives)));
            }
        }
    }
}
