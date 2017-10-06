package com.openxc.dweet.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.buglabs.dweetlib.DweetLib;
import com.openxc.sinks.DweetSink;
import com.openxc.sinks.VehicleDataSink;
import com.openxcplatform.dweet.R;

/**
 * Enable or disable sending of vehicle data to dweet.io.
 *
 * The thingname to send data is read from the shared
 * preferences.
 */
public class DweetingPreferenceManager extends VehiclePreferenceManager {
    private final static String TAG = "DweetPreferenceManager";
    private VehicleDataSink mDweeter;

    public DweetingPreferenceManager(Context context) {
        super(context);
    }

    public void close() {
        super.close();
        stopDweeting();
    }

    protected PreferenceListener createPreferenceListener() {
        return new PreferenceListener() {
            private int[] WATCHED_PREFERENCE_KEY_IDS = {
                R.string.dweeting_checkbox_key,
                R.string.dweeting_thingname_key,
                R.string.dweetproing_checkbox_key
            };

            protected int[] getWatchedPreferenceKeyIds() {
                return WATCHED_PREFERENCE_KEY_IDS;
            }

            public void readStoredPreferences() {
                setDweetingStatus(getPreferences().getBoolean(getString(
                                R.string.dweeting_checkbox_key), false),getPreferences().getBoolean(getString(
                        R.string.dweetproing_checkbox_key), false));
            }
        };
    }

    private void setDweetingStatus(boolean dweetEnabled, boolean proEnabled) {
        Log.i(TAG, "Setting dweet to " + dweetEnabled);
        Log.i(TAG, "Setting pro to " + proEnabled);

        SharedPreferences.Editor editor = getPreferences().edit();
        String thingname = getPreferenceString(R.string.dweeting_thingname_key);
        String thingkey = getPreferenceString(R.string.dweetproing_thingkey_key);
        String thingtoken = getPreferenceString(R.string.dweetproing_thingtoken_key);
        if (thingname == null || thingname.equals("")) {
            thingname = DweetLib.getInstance(getContext()).getRandomThingName();
            editor.putString(getString(R.string.dweeting_thingname_key), thingname);
            editor.putString(getString(R.string.dweeting_thingname_default), thingname);
            editor.apply();
        }
        if(dweetEnabled) {
            if(mDweeter != null) {
                stopDweeting();
            }

            try {
                if (proEnabled && (thingkey != null) && (thingtoken != null)) {
                    mDweeter = new DweetSink(getContext(), thingname, thingkey, thingtoken );
                } else {
                    mDweeter = new DweetSink(getContext(), thingname);
                }
            } catch(Exception e) {
                Log.w(TAG, "Unable to add dweet sink", e);
                return;
            }
            getVehicleManager().addSink(mDweeter);

        } else {
            stopDweeting();
        }
    }

    private void stopDweeting() {
        if(getVehicleManager() != null){
            Log.d(TAG,"removing Dweet sink");
            getVehicleManager().removeSink(mDweeter);
            mDweeter = null;
        }
    }
}
