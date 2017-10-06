package com.openxc.measurements;


import com.openxc.units.GeomagneticField;

public class PhoneMagnetometer extends BaseMeasurement<GeomagneticField>{

    public final static String ID = "phone_magnetometer";

    public PhoneMagnetometer(Number[] value) {
        super(new GeomagneticField(value));
    }

    public PhoneMagnetometer(Number x, Number y, Number z) { super(new GeomagneticField(x,y,z));}

    public PhoneMagnetometer(GeomagneticField value) {
        super(value);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
