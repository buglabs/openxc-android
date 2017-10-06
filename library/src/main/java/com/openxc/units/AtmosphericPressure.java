package com.openxc.units;

/**
 * lux, the SI unit measuring luminous flux per unit area. It is equal to one lumen per square metre.
 *
 * TODO this shouldn't represent two things.
 */
public class AtmosphericPressure extends Quantity<Number> {
    private final String TYPE_STRING = "hPa";

    public AtmosphericPressure(Number value) {
        super(value);
    }

    public AtmosphericPressure(Float value) {
        super(value);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
