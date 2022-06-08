package com.alcatrazescapee.primalwinter.platform;

import java.util.List;
import java.util.function.Supplier;

import com.alcatrazescapee.primalwinter.util.Config;

public abstract class AbstractConfig
{
    public void earlySetup() {}

    protected abstract Config.BooleanValue build(Config.Type configType, String name, boolean defaultValue, String comment);
    protected abstract Config.ListValue<String> build(Config.Type configType, String name, List<String> defaultValue, String comment);
    protected abstract Config.DoubleValue build(Config.Type configType, String name, double defaultValue, double minValue, double maxValue, String comment);
    protected abstract Config.IntValue build(Config.Type configType, String name, int defaultValue, int minValue, int maxValue, String comment);

    public interface BooleanValue extends Supplier<Boolean> {}
    public interface DoubleValue extends Supplier<Double> {}
    public interface IntValue extends Supplier<Integer> {}
    public interface ListValue<T> extends Supplier<List<T>> {}

    public enum Type
    {
        CLIENT,
        COMMON
    }
}
