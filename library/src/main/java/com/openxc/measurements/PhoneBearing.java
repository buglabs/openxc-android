package com.openxc.measurements;

import com.openxc.units.Degree;
import com.openxc.util.Range;

/**
 *
 * */
public class PhoneBearing extends BaseMeasurement<Degree> {

    private final static Range<Degree> RANGE =
            new Range<>(new Degree(0), new Degree(360));
    public final static String ID = "phone_bearing";

    public PhoneBearing(Number value) {
        super(new Degree(value));
    }

    public PhoneBearing(Degree value) {
        super(value);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
