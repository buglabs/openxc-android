package com.openxc.measurements;

import com.openxc.units.Illuminance;

/**
 * The Light level comes from from the phones light sensor, expressed in lux units
 */
public class LightLevel extends BaseMeasurement<Illuminance> {

    public final static String ID = "light_level";

    public LightLevel(Number value) {
        super(new Illuminance(value));
    }

    public LightLevel(Illuminance value) {
        super(value);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
