package com.openxc.measurements;


import com.openxc.units.AccelerometerForce;

public class PhoneAccelerometer extends BaseMeasurement<AccelerometerForce>{

    public final static String ID = "phone_accelerometer";

    public PhoneAccelerometer(Number[] value) {
        super(new AccelerometerForce(value));
    }

    public PhoneAccelerometer(Number x, Number y, Number z) { super(new AccelerometerForce(x,y,z));}

    public PhoneAccelerometer(AccelerometerForce value) {
        super(value);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
