package dev.rkulik.config;

import dev.rkulik.Operator;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CalculatorConfig {
        Map<String, Operator> getOperations();
        Map<String, Function<Double, Double>> getFunctions();
        Map<String, BiFunction<Double, Double, Double>> getBifunctions();
        Map<String, Function<List<Double>, Double>> getVariadicFunctions();
}
