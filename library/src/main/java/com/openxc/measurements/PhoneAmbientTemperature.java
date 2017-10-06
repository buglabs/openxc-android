package com.openxc.measurements;

import com.openxc.units.Degree;

public class PhoneAmbientTemperature extends BaseMeasurement<Degree>{

    public final static String ID = "phone_ambient_temperature";

    public PhoneAmbientTemperature(Number value) {
        super(new Degree(value));
    }

    public PhoneAmbientTemperature(Degree value) {
        super(value);
    }

    public String getGenericName() {
        return ID;
    }
}
