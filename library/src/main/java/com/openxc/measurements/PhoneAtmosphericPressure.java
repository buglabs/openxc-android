package com.openxc.measurements;

import com.openxc.units.AtmosphericPressure;
import com.openxc.util.Range;

/**
 *
 */
public class PhoneAtmosphericPressure extends BaseMeasurement<AtmosphericPressure>{

    private final static Range<AtmosphericPressure> RANGE =
            new Range<>(new AtmosphericPressure(870), new AtmosphericPressure(1085));
    public final static String ID = "phone_atmospheric_pressure";

    public PhoneAtmosphericPressure(Float value) {
        super(new AtmosphericPressure(value), RANGE);
    }

    public PhoneAtmosphericPressure(Number value) {
        super(new AtmosphericPressure(value), RANGE);
    }


    public PhoneAtmosphericPressure(AtmosphericPressure value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
