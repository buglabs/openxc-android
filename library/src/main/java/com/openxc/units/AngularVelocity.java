package com.openxc.units;

/**
 *
 */
public class AngularVelocity extends Cartesian<Number> {
    private final String TYPE_STRING = "rad/s";

    public AngularVelocity(Number[] value) {
        super(value);
    }

    public AngularVelocity(Number x, Number y, Number z) {
        super(x,y,z);
    }

    public String getTypeString() {
        return TYPE_STRING;
    }
}
