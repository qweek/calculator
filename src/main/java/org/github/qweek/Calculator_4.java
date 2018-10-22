package org.github.qweek;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.StreamSupport.stream;

public class Calculator_4 implements Calculator {
    private static final String DEFAULT_DELIMITER = ",";
    private static final Splitter DELIMITER_SPLITTER = Splitter.on('\n').limit(2);

    /**
     * 4. Support different delimiters.
     *   a. To change a delimiter, the beginning of the string will contain a separate line that looks like this: “//<delimiter>\n<numbers…>”, for example “//;\n1;2” should return 3 where the delimiter is ‘;’.
     *   b. The first line is optional, all existing scenarios should still be supported.
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
