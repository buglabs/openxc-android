package com.openxc.units;

/**
 * lux, the SI unit measuring luminous flux per unit area. It is equal to one lumen per square metre.
 *
 * TODO this shouldn't represent two things.
 */
public class Illuminance extends Quantity<Number> {
    private final String TYPE_STRING = "lux";

    public Illuminance(Number value) {
        super(value);
    }

    public Illuminance(Float value) {
        super(value);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
