package com.openxc.units;

/**
 *
 */
public class GeomagneticField extends Cartesian<Number> {
    private final String TYPE_STRING = "\u03BCT";

    public GeomagneticField(Number[] value) {
        super(value);
    }

    public GeomagneticField(Number x, Number y, Number z) {
        super(x,y,z);
    }

    public String getTypeString() {
        return TYPE_STRING;
    }
}
