package com.openxc.measurements;

import com.openxc.units.Percentage;
import com.openxc.util.Range;

/**
 *
 */
public class PhoneRelativeHumidity extends BaseMeasurement<Percentage>{

    private final static Range<Percentage> RANGE =
            new Range<>(new Percentage(0), new Percentage(100));
    public final static String ID = "phone_relative_humidity";

    public PhoneRelativeHumidity(Number value) {
        super(new Percentage(value), RANGE);
    }

    public PhoneRelativeHumidity(Percentage value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
