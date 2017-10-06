package com.openxc.measurements;

import com.openxc.units.MetersPerSecond;
import com.openxc.util.Range;

/**
 * Created by vish on 6/15/16.
 */
public class PhoneGPSSpeed extends BaseMeasurement<MetersPerSecond>{

    private final static Range<MetersPerSecond> RANGE =
            new Range<>(new MetersPerSecond(0), new MetersPerSecond(107527));
    public final static String ID = "phone_gps_speed";

    public PhoneGPSSpeed(Float value) {
        super(new MetersPerSecond(value), RANGE);
    }

    public PhoneGPSSpeed(Number value) {
        super(new MetersPerSecond(value), RANGE);
    }


    public PhoneGPSSpeed(MetersPerSecond value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
