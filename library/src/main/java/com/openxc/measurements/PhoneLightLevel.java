package com.openxc.measurements;

import com.openxc.units.Illuminance;
import com.openxc.util.Range;

/**
 * Created by vish on 6/15/16.
 */
public class PhoneLightLevel extends BaseMeasurement<Illuminance>{

    private final static Range<Illuminance> RANGE =
            new Range<>(new Illuminance(0), new Illuminance(107527));
    public final static String ID = "phone_light_level";

    public PhoneLightLevel(Float value) {
        super(new Illuminance(value), RANGE);
    }

    public PhoneLightLevel(Number value) {
        super(new Illuminance(value), RANGE);
    }


    public PhoneLightLevel(Illuminance value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
