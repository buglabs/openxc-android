package com.openxc.measurements;

import com.openxc.units.Meter;

/**
 *
 * */
public class PhoneAltitude extends BaseMeasurement<Meter> {

    public final static String ID = "phone_altitude";

    public PhoneAltitude(Number value) {
        super(new Meter(value));
    }

    public PhoneAltitude(Meter value) {
        super(value);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
