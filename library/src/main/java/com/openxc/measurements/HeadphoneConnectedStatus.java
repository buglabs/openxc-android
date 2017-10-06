package com.openxc.measurements;

/**
 * The HeadphoneConnectedStatus measurement knows if headphones are connected to the phone, or not.
 */
public class HeadphoneConnectedStatus extends BaseMeasurement<com.openxc.units.Boolean> {
    public final static String ID = "headphones_connected";

    public HeadphoneConnectedStatus(com.openxc.units.Boolean value) {
        super(value);
    }

    public HeadphoneConnectedStatus(java.lang.Boolean value) {
        this(new com.openxc.units.Boolean(value));
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
