package dev.rkulik.config;

import dev.rkulik.Operator;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class ExtendedCalculatorConfig implements CalculatorConfig {
    private final Map<String, Operator> operations = new HashMap<>();
    private final Map<String, Function<Double, Double>> functions = new HashMap<>();
    private final Map<String, BiFunction<Double, Double, Double>> bifunctions = new HashMap<>();
    private final Map<String, Function<List<Double>, Double>> variadicFunctions = new HashMap<>();

    public ExtendedCalculatorConfig() {
        defineOperators();
        defineFunctions();
    }

    private void defineOperators() {
        operations.put("+", Operator.addition());
        operations.put("-", Operator.subtraction());
        operations.put("*", Operator.multiplication());
        operations.put("/", Operator.division());
        operations.put("^", new Operator("^", 3, true, Math::pow));
        operations.put("#", new Operator("#", 2, false, (a, b) -> a * b + 2 * a)); // custom
    }

    private void defineFunctions() {
        functions.put("sin", Math::sin);
        functions.put("cos", Math::cos);
        functions.put("abs", Math::abs);
        functions.put("log", Math::log);

        bifunctions.put("pow", Math::pow);

        variadicFunctions.put("max", args -> args.stream().max(Double::compareTo).orElseThrow());
        variadicFunctions.put("min", args -> args.stream().min(Double::compareTo).orElseThrow());
    }
}
