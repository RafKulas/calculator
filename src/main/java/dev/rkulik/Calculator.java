package dev.rkulik;

import dev.rkulik.config.CalculatorConfig;
import dev.rkulik.exception.MissingBracketException;
import dev.rkulik.exception.UnsupportedCharacterException;
import dev.rkulik.exception.UnsupportedOperationException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Calculator {
    private final CalculatorConfig config;

    public double evaluate(String expression) {
        return new Expression(expression, config).parse();
    }

    static class Expression {
        private final String expr;
        private int pos = -1;
        private int ch = 0;
        private final CalculatorConfig config;

        Expression(String expr, CalculatorConfig config) {
            this.expr = expr;
            this.config = config;
        }

        public double parse() {
            nextChar();
            double x = parseExpression(0);
            if (pos < expr.length()) {
                throw new UnsupportedCharacterException((char) ch);
            }
            return x;
        }

        private void nextChar() {
            ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
        }

        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        private double parseExpression(int minPrecedence) {
            double left = parseUnary();

            while (true) {
                skipWhitespace();
                Operator op = getOperator();
                if (op == null || op.precedence < minPrecedence) {
                    break;
                }
                nextChar();
                int nextMinPrec = op.rightAssociative ? op.precedence : op.precedence + 1;
                double right = parseExpression(nextMinPrec);
                left = op.operation.apply(left, right);
            }
            return left;
        }

        private double parseUnary() {
            skipWhitespace();
            if (eat('+')) {
                return parseUnary();
            }
            if (eat('-')) {
                return -parseUnary();
            }
            return parsePrimary();
        }

        private double parsePrimary() {
            skipWhitespace();
            if (eat('(')) {
                double x = parseExpression(0);
                if (!eat(')')) {
                    throw new MissingBracketException();
                }
                return x;
            }

            if (Character.isLetter(ch)) {
                return applyFunction();
            }

            return consumeNumber();
        }

        private double applyFunction() {
            int start = pos;
            while (Character.isLetterOrDigit(ch)) nextChar();
            String name = expr.substring(start, pos);
            if (eat('(')) {
                List<Double> args = new ArrayList<>();
                if (!eat(')')) {
                    do {
                        args.add(parseExpression(0));
                    } while (eat(','));
                    if (!eat(')')) throw new MissingBracketException();
                }

                return applyOperator(name, args);
            } else {
                throw new MissingBracketException();
            }
        }

        private double consumeNumber() {
            if ((ch >= '0' && ch <= '9') || ch == '.') {
                int start = pos;
                while ((ch >= '0' && ch <= '9') || ch == '.') {
                    nextChar();
                }
                return Double.parseDouble(expr.substring(start, pos));
            }
            throw new UnsupportedCharacterException((char) ch);
        }

        private double applyOperator(String name, List<Double> args) {
            if (config.getVariadicFunctions().containsKey(name)) {
                return config.getVariadicFunctions().get(name).apply(args);
            } else if (args.size() == 1 && config.getFunctions().containsKey(name)) {
                return config.getFunctions().get(name).apply(args.getFirst());
            } else if (args.size() == 2 && config.getBifunctions().containsKey(name)) {
                return config.getBifunctions().get(name).apply(args.get(0), args.get(1));
            } else {
                throw new UnsupportedOperationException(name);
            }
        }

        private void skipWhitespace() {
            while (ch == ' ') nextChar();
        }

        private Operator getOperator() {
            skipWhitespace();
            String s = String.valueOf((char) ch);
            return config.getOperations().get(s);
        }
    }
}
