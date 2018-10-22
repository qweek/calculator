package org.github.qweek;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static java.util.stream.IntStream.empty;
import static java.util.stream.IntStream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CalculatorTest {

    private static Stream<Calculator> calculators() {
        return Stream.of(
                new Calculator_1(),
                new Calculator_2(),
                new Calculator_3(),
                new Calculator_4(),
                new Calculator_5(),
                new Calculator_6(),
                new Calculator_7(),
                new Calculator_8(),
                new Calculator_9()
        );
    }

    @DisplayName("Test sum for all calculators")
    @ParameterizedTest(name = "{index}: {arguments}.sum(...)")
    @MethodSource("calculators")
    void testSum(Calculator calculator) {
        assertEquals(0, calculator.sum(empty()));
        assertEquals(0, calculator.sum(null));
        assertEquals(1, calculator.sum(of(1)));
        assertEquals(3, calculator.sum(of(1, 2)));
        assertEquals(6, calculator.sum(of(1, 2, 3)));
    }

    @DisplayName("Test add for all calculators")
    @ParameterizedTest(name = "{index}: {arguments}.add(...)")
    @MethodSource("calculators")
    void testAdd(Calculator calculator) {
        assertEquals(0, calculator.add(null));
        assertEquals(0, calculator.add(""));
        assertEquals(1, calculator.add("1"));
        assertEquals(3, calculator.add("1,2"));

        assertEquals(0, calculator.add("0,0"));

        assertThrows(IllegalArgumentException.class, () -> calculator.add("a"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("a,2"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,b"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1;2"));
    }

    @Test
    void testCalculator_1() {
        Calculator calculator = new Calculator_1();

        testEmptyArguments(calculator);
        testNegativeNumbers(calculator);
        testMaxArguments(calculator);

        // supports only 0, 1 or 2 numbers
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,2,3"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add(",2,3"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,,3"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,2,"));
    }

    @Test
    void testCalculator_2() {
        Calculator calculator = new Calculator_2();

        testEmptyArguments(calculator);
        testNegativeNumbers(calculator);
        testMaxArguments(calculator);

        // supports multiple numbers (positive and negative)
        testMultipleNumbers(calculator);
        testMultipleNegativeNumbers(calculator);
    }

    @Test
    void testCalculator_3() {
        Calculator calculator = new Calculator_3();

        // empty arguments are not supported
        testEmptyArgumentsNotSupported(calculator);
        testNegativeNumbers(calculator);
        testMaxArguments(calculator);

        testMultipleNumbers(calculator);
        testMultipleNegativeNumbers(calculator);

        // supports comma and new line delimiters
        testNewLineDelimiter(calculator);
    }

    @Test
    void testCalculator_4() {
        Calculator calculator = new Calculator_4();

        testEmptyArgumentsNotSupported(calculator);
        testNegativeNumbers(calculator);
        testMaxArguments(calculator);

        testMultipleNumbers(calculator);
        testMultipleNegativeNumbers(calculator);

        // supports custom delimiters
        testCustomDelimiter(calculator);
        testSingleCharDelimiter(calculator);
    }

    @Test
    void testCalculator_5() {
        Calculator calculator = new Calculator_5();

        testEmptyArgumentsNotSupported(calculator);
        // negative numbers are not supported
        testNegativeNumbersNotSupported(calculator);
        testMaxArguments(calculator);

        testMultipleNumbers(calculator);
        // negative numbers are not supported
        testMultipleNegativeNumbersNotSupported(calculator);

        testCustomDelimiter(calculator);
        testSingleCharDelimiter(calculator);
    }

    @Test
    void testCalculator_6() {
        Calculator calculator = new Calculator_6();

        testEmptyArgumentsNotSupported(calculator);
        testNegativeNumbersNotSupported(calculator);
        testMaxArgumentsNotSupported(calculator);

        // supports numbers <= 1000
        testMultipleNumbersLessThanThousand(calculator);
        testMultipleNegativeNumbersNotSupported(calculator);

        testCustomDelimiter(calculator);
        testSingleCharDelimiter(calculator);
    }

    @Test
    void testCalculator_7() {
        Calculator calculator = new Calculator_7();

        testEmptyArgumentsNotSupported(calculator);
        testNegativeNumbersNotSupported(calculator);
        testMaxArgumentsNotSupported(calculator);

        testMultipleNumbersLessThanThousand(calculator);
        testMultipleNegativeNumbersNotSupported(calculator);

        testCustomDelimiter(calculator);
        // support delimiter with multiple chars
        testMultipleCharDelimiter(calculator);
        testMultipleDelimitersNotSupported(calculator);
    }

    @Test
    void testCalculator_8() {
        Calculator calculator = new Calculator_8();

        testEmptyArgumentsNotSupported(calculator);
        testNegativeNumbersNotSupported(calculator);
        testMaxArgumentsNotSupported(calculator);

        testMultipleNumbersLessThanThousand(calculator);
        testMultipleNegativeNumbersNotSupported(calculator);

        testCustomDelimiter(calculator);
        // delimiters with multiple chars are not supported
        testSingleCharDelimiter(calculator);
        // support multiple delimiters
        testMultipleDelimiters(calculator);

        assertThrowsWithMessage("Delimiter too long", () -> calculator.add("//ab|c\n100ab20c3"));
    }

    @Test
    void testCalculator_9() {
        Calculator calculator = new Calculator_9();

        testEmptyArgumentsNotSupported(calculator);
        testNegativeNumbersNotSupported(calculator);
        testMaxArgumentsNotSupported(calculator);

        testMultipleNumbersLessThanThousand(calculator);
        testMultipleNegativeNumbersNotSupported(calculator);

        testCustomDelimiter(calculator);
        // support delimiter with multiple chars
        testMultipleCharDelimiter(calculator);
        testMultipleDelimiters(calculator);

        // support multiple delimiters with multiple chars
        testComplexDelimiters(calculator);
    }

    private void testEmptyArguments(Calculator calculator) {
        assertEquals(0, calculator.add(","));
        assertEquals(1, calculator.add(",1"));
        assertEquals(1, calculator.add("1,"));
    }

    private void testMaxArguments(Calculator calculator) {
        assertEquals(2147483647, calculator.add("0,2147483647"));
        assertEquals(2147483647, calculator.add("147483647,2000000000"));

        assertThrows(ArithmeticException.class, () -> calculator.add("1,2147483647"));

        assertThrows(IllegalArgumentException.class, () -> calculator.add("2147483648"));
    }

    private void testNegativeNumbers(Calculator calculator) {
        assertEquals(-42, calculator.add("-42"));
        assertEquals(-210, calculator.add("-10,-200"));
        assertEquals(-190, calculator.add("10,-200"));
        assertEquals(190, calculator.add("-10,200"));
        assertEquals(0, calculator.add("-2147483647,2147483647"));
        assertEquals(-1, calculator.add("-2147483648,2147483647"));
    }

    private void testMultipleNumbers(Calculator calculator) {
        assertEquals(6, calculator.add("1,2,3"));
        assertEquals(123, calculator.add("100,20,3"));
        assertEquals(2147483647, calculator.add("2000000000,100000000,40000000,7000000,400000,80000,3000,600,40,7"));
    }

    private void testMultipleNegativeNumbers(Calculator calculator) {
        assertEquals(-2, calculator.add("-1,2,-3"));
        assertEquals(83, calculator.add("100,-20,3"));
        assertEquals(-2147483648, calculator.add("-2000000000,-100000000,-40000000,-7000000,-400000,-80000,-3000,-600,-40,-8"));
    }

    private void testEmptyArgumentsNotSupported(Calculator calculator) {
        assertThrows(IllegalArgumentException.class, () -> calculator.add(","));
        assertThrows(IllegalArgumentException.class, () -> calculator.add(",1"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,,3"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1,\n"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("1\n\n3"));
    }

    private void testNewLineDelimiter(Calculator calculator) {
        assertEquals(6, calculator.add("1\n2,3"));
        assertEquals(321, calculator.add("1\n20\n300"));
        assertEquals(4321, calculator.add("1,20\n300,4000"));
    }

    private void testCustomDelimiter(Calculator calculator) {
        // supports empty string with custom delimiter
        assertEquals(0, calculator.add("//;"));
        assertEquals(0, calculator.add("//;\n"));

        // empty delimiter is not supported
        assertThrowsWithMessage("Delimiter is empty", () -> calculator.add("//"));
        assertThrowsWithMessage("Delimiter is empty", () -> calculator.add("//\n"));

        // empty arguments are not supported
        assertThrows(IllegalArgumentException.class, () -> calculator.add("//;\n;"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("//;\n1;"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("//;\n;2"));

        // supports different delimiters
        assertEquals(321, calculator.add("//;\n1;20;300"));
        assertEquals(123, calculator.add("//:\n100:20:3"));
    }

    private void testSingleCharDelimiter(Calculator calculator) {
        // long delimiters not supported
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> calculator.add("//;;\n1;;20;;300"));
        assertEquals("Delimiter too long", thrown.getMessage());
    }

    private void testNegativeNumbersNotSupported(Calculator calculator) {
        assertThrowsWithMessage("negatives not allowed: -42", () -> calculator.add("-42"));
        assertThrowsWithMessage("negatives not allowed: -10,-200", () -> calculator.add("-10,-200"));
        assertThrowsWithMessage("negatives not allowed: -200", () -> calculator.add("10,-200"));
        assertThrowsWithMessage("negatives not allowed: -10", () -> calculator.add("-10,200"));
        assertThrowsWithMessage("negatives not allowed: -2147483647", () -> calculator.add("-2147483647,2147483647"));
        assertThrowsWithMessage("negatives not allowed: -2147483648", () -> calculator.add("-2147483648,2147483647"));
    }

    private void testMultipleNegativeNumbersNotSupported(Calculator calculator) {
        assertThrowsWithMessage("negatives not allowed: -1,-3", () -> calculator.add("-1,2,-3"));
        assertThrowsWithMessage("negatives not allowed: -20", () -> calculator.add("100,-20,3"));
        assertThrowsWithMessage("negatives not allowed: -2000000000,-100000000,-40000000,-7000000,-400000,-80000,-3000,-600,-40,-8", () -> calculator.add("-2000000000,-100000000,-40000000,-7000000,-400000,-80000,-3000,-600,-40,-8"));
        assertThrowsWithMessage("negatives not allowed: -1,-3,-5,-7,-9", () -> calculator.add("-1,2,-3,4,-5,6,-7,8,-9"));
    }

    private void testMaxArgumentsNotSupported(Calculator calculator) {
        assertEquals(0, calculator.add("0,2147483647"));
        assertEquals(0, calculator.add("147483647,2000000000"));

        assertEquals(1, calculator.add("1,2147483647"));

        assertThrows(IllegalArgumentException.class, () -> calculator.add("2147483648"));
    }

    private void testMultipleNumbersLessThanThousand(Calculator calculator) {
        assertEquals(6, calculator.add("1,2,3"));
        assertEquals(123, calculator.add("100,20,3"));
        assertEquals(647, calculator.add("2000000000,100000000,40000000,7000000,400000,80000,3000,600,40,7"));

        assertEquals(1001, calculator.add("2,999"));
        assertEquals(1002, calculator.add("2,1000"));
        assertEquals(2, calculator.add("2,1001"));
    }

    private void testMultipleCharDelimiter(Calculator calculator) {
        assertEquals(321, calculator.add("//;;\n1;;20;;300"));
        assertEquals(6, calculator.add("//***\n1***2***3"));
        assertEquals(123, calculator.add("//abc\n100abc20abc3"));

        assertThrows(IllegalArgumentException.class, () -> calculator.add("//;;\n1;20;;300"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("//***\n1**a2***3"));
        assertThrows(IllegalArgumentException.class, () -> calculator.add("//abc\n100abcd20abc3"));
    }

    private void testMultipleDelimitersNotSupported(Calculator calculator) {
        assertEquals(123, calculator.add("//|\n100|20|3"));
        assertEquals(123, calculator.add("//a|\n100a|20a|3"));
        assertEquals(123, calculator.add("//a|c\n100a|c20a|c3"));

        assertThrows(IllegalArgumentException.class, () -> calculator.add("//a|c\n100a20c3"));
    }

    private void testMultipleDelimiters(Calculator calculator) {
        assertEquals(123, calculator.add("//a|c\n100a20c3"));
        assertEquals(6, calculator.add("//*|%\n1*2%3"));
    }

    private void testComplexDelimiters(Calculator calculator) {
        assertEquals(123, calculator.add("//ab|c\n100ab20c3"));
        assertEquals(4995, calculator.add("//abc|def|ghi|jkl|mno|pqr|stu|vwx|yz\n999abc888def777ghi666jkl555mno444pqr333stu222vwx111yz0"));
        assertEquals(1110, calculator.add("//***|%%%|???\n111***222%%%333???444"));
    }

    private void assertThrowsWithMessage(String message, Executable executable) {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(message, thrown.getMessage());
    }
}
