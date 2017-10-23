package com.openxc.sources;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.common.base.MoreObjects;
import com.openxc.measurements.PhoneAccelerometer;
import com.openxc.measurements.PhoneAltitude;
import com.openxc.measurements.PhoneAmbientTemperature;
import com.openxc.measurements.PhoneAtmosphericPressure;
import com.openxc.measurements.PhoneBearing;
import com.openxc.measurements.PhoneGPSSpeed;
import com.openxc.measurements.PhoneGyroscope;
import com.openxc.measurements.PhoneHeadphoneConnected;
import com.openxc.measurements.PhoneLightLevel;
import com.openxc.measurements.PhoneMagnetometer;
import com.openxc.measurements.PhoneProximity;
import com.openxc.measurements.PhoneRelativeHumidity;
import com.openxc.measurements.PhoneRotation;
import com.openxc.messages.SimpleVehicleMessage;
import com.openxc.messages.VehicleMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Generate location measurements based on native GPS updates.
 *
 * This source listens for GPS location updates from the built-in Android location
 * framework and passes them to the OpenXC vehicle measurement framework as if
 * they originated from the vehicle. This source is useful to seamlessly use
 * location in an application regardless of it the vehicle has built-in GPS.
 *
 * The ACCESS_FINE_LOCATION permission is required to use this source.
 */
public class PhoneSensorSource extends ContextualVehicleDataSource
        implements SensorEventListener, LocationListener, Runnable {
    private final static String TAG = "PhoneSensorSource";
    private final static int DEFAULT_INTERVAL = 5000;

    private Looper mLooper;

    private static LocationManager locationManager;

    private static SensorManager sensorService;
    private Sensor sensor;
    private float ax, ay, az;
    private float rx, ry, rz, rw, racc;
    private float gx, gy, gz;
    private float mx, my, mz;
    private float light;
    private float proximity;
    private float humidity;
    private float pressure;
    private float temperature;
    private double altitude, bearing, speed;
    private Context thecontext;

    String devmodel, devname, osver;

    public PhoneSensorSource(SourceCallback callback, Context context) {
        super(callback, context);

        thecontext = context;

        devmodel = Build.MODEL;
        devname = Build.PRODUCT;
        osver = Build.VERSION.RELEASE;

        System.out.println("MODEL: " + android.os.Build.MODEL
                + "\nDEVICE: " + android.os.Build.DEVICE
                + "\nBRAND: " + android.os.Build.BRAND
                + "\nDISPLAY: " + android.os.Build.DISPLAY
                + "\nBOARD: " + android.os.Build.BOARD
                + "\nHOST: " + android.os.Build.HOST
                + "\nMANUFACTURER: " + android.os.Build.MANUFACTURER
                + "\nPRODUCT: " + android.os.Build.PRODUCT);

        PackageManager PM = context.getPackageManager();
        boolean gyro = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        System.out.println("gyro allowed:" + gyro);
        sensorService = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);


        List<Sensor> listSensor = sensorService.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < listSensor.size(); i++) {
            System.out.println("Sensor : " + listSensor.get(i).getName());
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


    }

    public PhoneSensorSource(Context context) {
        this(null, context);
    }

    @Override
    public void run() {
        Looper.prepare();

        Log.w(TAG, "Phone Sensor Looper started");

        try {
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_LIGHT),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(this,
                    sensorService.getDefaultSensor(Sensor.TYPE_PRESSURE),
                    SensorManager.SENSOR_DELAY_NORMAL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                sensorService.registerListener(this,
                        sensorService.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                        SensorManager.SENSOR_DELAY_NORMAL);
                sensorService.registerListener(this,
                        sensorService.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                        SensorManager.SENSOR_DELAY_NORMAL);
            }

            if (ActivityCompat.checkSelfPermission(thecontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thecontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, DEFAULT_INTERVAL, 10, this);


        } catch(IllegalArgumentException e) {
            Log.w(TAG, "Problem registering Sensor");
        }

        mLooper = Looper.myLooper();
        Looper.loop();
    }

    @Override
    public void stop() {
        super.stop();
        System.out.println("Phone Sensor service stopped");
        onPipelineDeactivated();
        sensorService.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //System.out.println("Sensor update:"+event.sensor.getName());
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float[] accelValues = event.values;
            ax = accelValues[0];
            ay = accelValues[1];
            az = accelValues[2];

            handleMessage(new SimpleVehicleMessage(PhoneAccelerometer.ID+"_x",ax));
            handleMessage(new SimpleVehicleMessage(PhoneAccelerometer.ID+"_y",ay));
            handleMessage(new SimpleVehicleMessage(PhoneAccelerometer.ID+"_z",az));
//            handleMessage(new SimpleVehicleMessage(PhoneAccelerometer.ID,
//                    accelValues));
            //handleMessage(new SimpleVehicleMessage(System.currentTimeMillis(),PhoneAccelerometer.ID,accel));

        }
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            float[] rotationValues = event.values;
            rx = rotationValues[0];
            ry = rotationValues[1];
            rz = rotationValues[2];
            rw = rotationValues[3];
            racc = rotationValues[4];

//            handleMessage(new SimpleVehicleMessage(PhoneRotation.ID,
//                    rotationValues));
            handleMessage(new SimpleVehicleMessage(PhoneRotation.ID+"_x",
                    rx));
            handleMessage(new SimpleVehicleMessage(PhoneRotation.ID+"_y",
                    ry));
            handleMessage(new SimpleVehicleMessage(PhoneRotation.ID+"_z",
                    rz));
            handleMessage(new SimpleVehicleMessage(PhoneRotation.ID+"_w",
                    rw));
            handleMessage(new SimpleVehicleMessage(PhoneRotation.ID+"_acc",
                    racc));
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float[] gyroValues = event.values;
            gx = gyroValues[0];
            gy = gyroValues[1];
            gz = gyroValues[2];

            //handleMessage(new SimpleVehicleMessage(PhoneGyroscope.ID, gyroValues));
            handleMessage(new SimpleVehicleMessage(PhoneGyroscope.ID+"_x", gx));
            handleMessage(new SimpleVehicleMessage(PhoneGyroscope.ID+"_y", gy));
            handleMessage(new SimpleVehicleMessage(PhoneGyroscope.ID+"_z", gz));
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            float[] magnetoValues = event.values;
            mx = magnetoValues[0];
            my = magnetoValues[1];
            mz = magnetoValues[2];

//            handleMessage(new SimpleVehicleMessage(PhoneMagnetometer.ID, magnetoValues));
            handleMessage(new SimpleVehicleMessage(PhoneMagnetometer.ID+"_x", mx));
            handleMessage(new SimpleVehicleMessage(PhoneMagnetometer.ID+"_y", my));
            handleMessage(new SimpleVehicleMessage(PhoneMagnetometer.ID+"_z", mz));
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            //   System.out.println("new light: " + event.values[0]);
            light = event.values[0];

            handleMessage(new SimpleVehicleMessage(PhoneLightLevel.ID, light));
        }
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //   System.out.println("new light: " + event.values[0]);
            proximity = event.values[0];
            handleMessage(new SimpleVehicleMessage(PhoneProximity.ID, proximity));
        }
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            //   System.out.println("new light: " + event.values[0]);
            pressure = event.values[0];
            handleMessage(new SimpleVehicleMessage(PhoneAtmosphericPressure.ID, pressure));
        }
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            //   System.out.println("new light: " + event.values[0]);
            humidity = event.values[0];
            handleMessage(new SimpleVehicleMessage(PhoneRelativeHumidity.ID, humidity));
        }
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            //   System.out.println("new light: " + event.values[0]);
            temperature = event.values[0];
            handleMessage(new SimpleVehicleMessage(PhoneAmbientTemperature.ID, temperature));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status,
            Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public boolean isConnected() {

        return (sensorService != null);
    }

    @Override
    public void onPipelineActivated() {
        Log.i(TAG, "Enabling phone sensor collection");
        new Thread(this).start();
    }

    @Override
    public void onPipelineDeactivated() {
        Log.i(TAG, "Disabled phone sensor collection");
        if(mLooper != null) {
            mLooper.quit();
        }
        locationManager.removeUpdates(this);
        sensorService.unregisterListener(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("updateInterval",DEFAULT_INTERVAL)
            .toString();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        System.out.println("accuracy changed");
    }

    @Override
    public void onLocationChanged(Location location) {
        altitude = location.getAltitude();
        bearing = location.getBearing();
        speed = location.getSpeed();
        handleMessage(new SimpleVehicleMessage(PhoneAltitude.ID, altitude));
        handleMessage(new SimpleVehicleMessage(PhoneBearing.ID, bearing));
        handleMessage(new SimpleVehicleMessage(PhoneGPSSpeed.ID, speed));
    }
}
