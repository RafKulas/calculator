package dev.rkulik.config;

import dev.rkulik.Operator;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class SimpleCalculatorConfig implements CalculatorConfig {
    private final Map<String, Operator> operations = new HashMap<>();

    public SimpleCalculatorConfig() {
        defineOperators();
    }

    private void defineOperators() {
        operations.put("+", Operator.addition());
        operations.put("-", Operator.subtraction());
        operations.put("*", Operator.multiplication());
        operations.put("/", Operator.division());
    }

    public Map<String, Operator> getOperations() { return operations; }
    public Map<String, Function<Double, Double>> getFunctions() { return Collections.emptyMap(); }
    public Map<String, BiFunction<Double, Double, Double>> getBifunctions() { return Collections.emptyMap(); }
    public Map<String, Function<List<Double>, Double>> getVariadicFunctions() { return Collections.emptyMap(); }
}
