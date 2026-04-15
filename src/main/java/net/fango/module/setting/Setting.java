package net.fango.module.setting;

public class Setting<T> {
    public enum Type { BOOLEAN, INTEGER, DOUBLE, ENUM, STRING }

    private final String name;
    private final Type   type;
    private       T      value;
    private final T      min;
    private final T      max;
    private final T[]    values;

    public static Setting<Boolean> ofBool(String name, boolean def) {
        return new Setting<>(name, Type.BOOLEAN, def, null, null, null);
    }
    public static Setting<Integer> ofInt(String name, int def, int min, int max) {
        return new Setting<>(name, Type.INTEGER, def, min, max, null);
    }
    public static Setting<Double> ofDouble(String name, double def, double min, double max) {
        return new Setting<>(name, Type.DOUBLE, def, min, max, null);
    }
    @SafeVarargs
    public static <E> Setting<E> ofEnum(String name, E def, E... values) {
        return new Setting<>(name, Type.ENUM, def, null, null, values);
    }
    public static Setting<String> ofString(String name, String def) {
        return new Setting<>(name, Type.STRING, def, null, null, null);
    }

    private Setting(String name, Type type, T value, T min, T max, T[] values) {
        this.name   = name;
        this.type   = type;
        this.value  = value;
        this.min    = min;
        this.max    = max;
        this.values = values;
    }

    public String getName()    { return name; }
    public Type   getType()    { return type; }
    public T      getValue()   { return value; }
    public void   setValue(T v){ value = v; }
    public T      getMin()     { return min; }
    public T      getMax()     { return max; }
    public T[]    getValues()  { return values; }
}
