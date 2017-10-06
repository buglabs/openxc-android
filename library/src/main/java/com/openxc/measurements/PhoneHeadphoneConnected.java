package com.openxc.measurements;

/**
 * The BrakePedalStatus measurement knows if the brake pedal is pressed.
 */
public class PhoneHeadphoneConnected extends BaseMeasurement<com.openxc.units.Boolean> {
    public final static String ID = "phone_headphone_connected";

    public PhoneHeadphoneConnected(com.openxc.units.Boolean value) {
        super(value);
    }

    public PhoneHeadphoneConnected(Boolean value) {
        this(new com.openxc.units.Boolean(value));
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
