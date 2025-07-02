package dev.rkulik;

import java.util.function.BinaryOperator;

public final class Operator {
    final String symbol;
    final int precedence;
    final boolean rightAssociative;
    final BinaryOperator<Double> operation;

    public Operator(
            String symbol,
            int precedence,
            boolean rightAssociative,
            BinaryOperator<Double> operation) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.rightAssociative = rightAssociative;
        this.operation = operation;
    }

    public static Operator addition() {
        return new Operator("+", 1, false, Double::sum);
    }

    public static Operator subtraction() {
        return new Operator("-", 1, false, (a, b) -> a - b);
    }

    public static Operator multiplication() {
        return new Operator("*", 2, false, (a, b) -> a * b);
    }

    public static Operator division() {
        return new Operator("/", 2, false, (a, b) -> a / b);
    }
}
