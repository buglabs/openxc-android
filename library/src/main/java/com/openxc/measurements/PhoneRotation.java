package com.openxc.measurements;


import com.openxc.units.Cartesian;

public class PhoneRotation extends BaseMeasurement<Cartesian>{

    public final static String ID = "phone_rotation";

    public PhoneRotation(Number[] value) {
        super(new Cartesian<Number>(value));
    }

    public PhoneRotation(Number x, Number y, Number z) { super(new Cartesian(x,y,z));}

    public PhoneRotation(Cartesian<Number> value) {
        super(value);
    }

    public String getGenericName() {
        return ID;
    }
}
