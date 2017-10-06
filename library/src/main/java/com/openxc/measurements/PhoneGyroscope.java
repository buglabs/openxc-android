package com.openxc.measurements;


import com.openxc.units.AngularVelocity;

public class PhoneGyroscope extends BaseMeasurement<AngularVelocity>{

    public final static String ID = "phone_gyroscope";

    public PhoneGyroscope(Number[] value) {
        super(new AngularVelocity(value));
    }

    public PhoneGyroscope(Number x, Number y, Number z) { super(new AngularVelocity(x,y,z));}

    public PhoneGyroscope(AngularVelocity value) {
        super(value);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
