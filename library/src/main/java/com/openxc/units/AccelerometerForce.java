package com.openxc.units;

/**
 *
 */
public class AccelerometerForce extends Cartesian<Number> {
    private final String TYPE_STRING = "m/s\u00B2";

    public AccelerometerForce(Number[] value) {
        super(value);
    }

    public AccelerometerForce(Number x, Number y, Number z) {
        super(x,y,z);
    }

    public String getTypeString() {
        return TYPE_STRING;
    }
}
