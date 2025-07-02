package dev.rkulik;

import dev.rkulik.config.CalculatorConfig;
import dev.rkulik.config.ExtendedCalculatorConfig;
import dev.rkulik.config.SimpleCalculatorConfig;
import dev.rkulik.exception.MissingBracketException;
import dev.rkulik.exception.UnsupportedCharacterException;
import dev.rkulik.exception.UnsupportedOperationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CalculatorTest {

    private static final CalculatorConfig SIMPLE_CALCULATOR_CONFIG = new SimpleCalculatorConfig();
    private static final CalculatorConfig EXTENDED_CALCULATOR_CONFIG = new ExtendedCalculatorConfig();
    @Nested
    class ExceptionTests {
        @Test
        void givenSimpleCalculator_whenParsingExpressionWithUnknownCharacter_thenThrowUnknownCharacterException() {
            // GIVEN
            final Calculator calculator = new Calculator(SIMPLE_CALCULATOR_CONFIG);
            final String expression = "1 simplify to 2";

            // WHEN-THEN
            assertThatThrownBy(() -> calculator.evaluate(expression))
                    .isInstanceOf(UnsupportedCharacterException.class)
                    .hasMessageContaining("Unsupported character appearance: s");
        }

        @Test
        void givenExtendedCalculator_whenParsingExpressionWithUnknownCharacter_thenThrowUnknownCharacterException() {
            // GIVEN
            final Calculator calculator = new Calculator(EXTENDED_CALCULATOR_CONFIG);
            final String expression = "1 & 2";

            // WHEN-THEN
            assertThatThrownBy(() -> calculator.evaluate(expression))
                    .isInstanceOf(UnsupportedCharacterException.class)
                    .hasMessageContaining("Unsupported character appearance: &");
        }

        @Test
        void givenCalculator_whenParsingExpressionWithMissingBracket_thenThrowMissingBracketException() {
            // GIVEN
            final Calculator calculator = new Calculator(SIMPLE_CALCULATOR_CONFIG);
            final String expression = "(1 + 2) * (2 + 3";

            // WHEN-THEN
            assertThatThrownBy(() -> calculator.evaluate(expression))
                    .isInstanceOf(MissingBracketException.class)
                    .hasMessageContaining("Missing ')'");
        }

        @Test
        void givenExtendedCalculator_whenParsingExpressionWithUnknownFunction_thenThrowUnknownOperationException() {
            // GIVEN
            final Calculator calculator = new Calculator(EXTENDED_CALCULATOR_CONFIG);
            final String expression = "magic(2)";

            // WHEN-THEN
            assertThatThrownBy(() -> calculator.evaluate(expression))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("Unsupported operation appearance: magic");
        }
    }

    @Nested
    class SimpleCalculatorTests {
        final Calculator sut = new Calculator(SIMPLE_CALCULATOR_CONFIG);

        @ParameterizedTest(name = "{0} = {1}")
        @CsvSource({
                "'1 + 2', 3.0",
                "'2 * 3 + 4', 10.0",
                "'2 + 3 * 4', 14.0",
                "'10 / 2 + 5', 10.0",
                "'5 + 10 / 2', 10.0",
                "'(1 + 2) * 3', 9.0",
                "'1 + (2 * 3)', 7.0",
                "'(2 + 3) * (4 + 5)', 45.0",
                "'10 - 4 - 1', 5.0",
                "'10 - (4 - 1)', 7.0",
                "'3 + 4 * 2 / (1 - 5)', 1.0"
        })
        void parametrizedTest(String expression, double expected) {
            // WHEN
            final var result = sut.evaluate(expression);

            // THEN
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    class ExtendedCalculatorTests {
        final Calculator sut = new Calculator(EXTENDED_CALCULATOR_CONFIG);

        @ParameterizedTest(name = "{0} = {1}")
        @CsvSource({
                "'1 + 2', 3.0",
                "'2 * 3 + 4', 10.0",
                "'2 + 3 * 4', 14.0",
                "'10 / 2 + 5', 10.0",
                "'5 + 10 / 2', 10.0",
                "'(1 + 2) * 3', 9.0",
                "'1 + (2 * 3)', 7.0",
                "'(2 + 3) * (4 + 5)', 45.0",
                "'10 - 4 - 1', 5.0",
                "'10 - (4 - 1)', 7.0",
                "'3 + 4 * 2 / (1 - 5)', 1.0",
                "'2 ^ 3', 8.0",
                "'2 # 3', 10",
                "'sin(0)', 0.0",
                "'sin(3.141592653589793 / 2)', 1.0",
                "'max(1, 5, 3)', 5.0",
                "'max(1+2, 2*3, 3+3)', 6.0"
        })
        void parametrizedTest(String expression, double expected) {
            // WHEN
            final var result = sut.evaluate(expression);

            // THEN
            assertThat(result).isEqualTo(expected);
        }
    }
}