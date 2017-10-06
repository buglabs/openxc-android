package com.openxc.measurements;

import com.openxc.units.Meter;
import com.openxc.util.Range;

/**
 * Created by vish on 6/15/16.
 */
public class PhoneProximity extends BaseMeasurement<Meter>{

    private final static Range<Meter> RANGE =
            new Range<>(new Meter(0), new Meter(107527));
    public final static String ID = "phone_proximity";

    public PhoneProximity(Number value) {
        super(new Meter(value), RANGE);
    }

    public PhoneProximity(Meter value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
