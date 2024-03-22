package expression.generic.elements;

import java.util.function.BiFunction;
import java.util.function.Function;

public record ModeConfig<E>(
        BiFunction<E, E, E> add,
        BiFunction<E, E, E> subtract,
        BiFunction<E, E, E> multiply,
        BiFunction<E, E, E> divide,
        Function<E, E> negate,
        Function<Integer, E> converter,
        Function<E, Integer> backConverter,
        Function<E, E> count,
        BiFunction<E, E, E> min,
        BiFunction<E, E, E> max

) {}