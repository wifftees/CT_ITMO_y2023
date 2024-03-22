package expression.generic.elements;

import expression.generic.elements.ModeConfig;

public interface ConfigGenerator<T> {
    ModeConfig<T> getConfig();
}
