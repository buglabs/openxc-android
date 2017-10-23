package com.openxc.dweet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Build;


import com.bugsnag.android.Bugsnag;
import com.openxcplatform.dweet.BuildConfig;
import com.openxc.VehicleManager;
import com.openxc.dweet.preferences.PreferenceManagerService;
import com.openxcplatform.dweet.R;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


/** The OpenXC Enabler app is primarily for convenience, but it also increases
 * the reliability of OpenXC by handling background tasks on behalf of client
 * applications.
 *
 * The Enabler provides a common location to control which data sources and
 * sinks are active, e.g. if the a trace file should be played back or recorded.
 * It's preferable to be able to change the data source on the fly, and not have
 * to programmatically load a trace file in any application under test.
 *
 * With the Enabler installed, the {@link com.openxc.remote.VehicleService} is
 * also started automatically when the Android device boots up. A simple data
 * sink like a trace file uploader can start immediately without any user
 * interaction.
 *
 * As a developer, you can also appreciate that because the Enabler takes care
 * of starting the {@link com.openxc.remote.VehicleService}, you don't need to
 * add much to your application's AndroidManifest.xml - just the
 * {@link com.openxc.VehicleManager} service.
*/
public class OpenXcEnablerActivity extends FragmentActivity {
    private static String TAG = "OpenXcEnablerActivity";

    private EnablerFragmentAdapter mAdapter;
    private ViewPager mPager;

    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String>  permissionsRejected;
    private final static int FINE_LOCATION_RESULT = 101;
    private final static int WRITE_EXTERNAL_RESULT = 102;
    private final static int READ_EXTERNAL_RESULT = 103;
    private final static int ALL_PERMISSIONS_RESULT = 104;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String bugsnagToken = BuildConfig.BUGSNAG_TOKEN;
        if(bugsnagToken != null && !bugsnagToken.isEmpty()) {
            try {
                Bugsnag.init(this, bugsnagToken);
            } catch(NoClassDefFoundError e) {
                Log.w(TAG, "Busgnag is unsupported when building from Eclipse", e);
            }
        } else {
            Log.i(TAG, "No Bugsnag token found in AndroidManifest, not enabling Bugsnag");
        }

        Log.i(TAG, "OpenXC Dweet created");
        setContentView(R.layout.main);
        mAdapter = new EnablerFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mPager.setCurrentItem(savedInstanceState.getInt("tab", 0));
        }

        startService(new Intent(this, VehicleManager.class));
        startService(new Intent(this, PreferenceManagerService.class));

        if (isMarshorAbove()) {
            int permsRequestCode = 200;
            ArrayList<String> permissions = new ArrayList<>();
            permissions.add(ACCESS_FINE_LOCATION);
            permissions.add(WRITE_EXTERNAL_STORAGE);
            permissions.add(READ_EXTERNAL_STORAGE);
            int resultCode = ALL_PERMISSIONS_RESULT;
            permissionsToRequest = filterPermissions(permissions);
            if (permissionsToRequest != null && permissionsToRequest.size()>0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), resultCode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", mPager.getCurrentItem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.settings:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public static class EnablerFragmentAdapter extends FragmentPagerAdapter {
        private static final String[] mTitles = { "Status", "Dashboard",
            "CAN", "Diagnostic", "Send CAN" };

        public EnablerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 1) {
                return new VehicleDashboardFragment();
            } else if(position == 2) {
                return new CanMessageViewFragment();
            } else if(position == 3) {
                return new DiagnosticRequestFragment();
            } else if(position == 4) {
                return new SendCanMessageFragment();
            }

            // For position 0 or anything unrecognized, go to Status
            return new StatusFragment();
        }
    }

    static String getBugsnagToken(Context context) {
        String key = null;
        try {
            Context appContext = context.getApplicationContext();
            ApplicationInfo appInfo = appContext.getPackageManager().getApplicationInfo(
                    appContext.getPackageName(), PackageManager.GET_META_DATA);
            if(appInfo.metaData != null) {
                key = appInfo.metaData.getString("com.bugsnag.token");
            }
        } catch (NameNotFoundException e) {
            // Should not happen since the name was determined dynamically from the app context.
            Log.e(TAG, "Unexpected NameNotFound.", e);
        }
        return key;
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case ALL_PERMISSIONS_RESULT:

                boolean someAccepted = false;
                boolean someRejected = false;
                if(permissionsRejected == null)
                    permissionsRejected = new ArrayList<String>();

                for(String perms : permissionsToRequest){
                    if(hasPermission(perms)){
                        someAccepted = true;
                    }else{
                        someRejected = true;
                        permissionsRejected.add(perms);
                    }
                }

                if(permissionsRejected.size()>0){
                    someRejected = true;
                }

                if(someAccepted){
                }
                if(someRejected){
                    Log.w(TAG,"Some permissions rejected by user, app may experience issues");
                }
                break;

        }

    }

    /**
     * This method is used to determine the permissions we do not have accepted yet.
     * @param wanted
     * @return
     */
    private ArrayList<String> filterPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * method that will return whether the permission is accepted. By default it is true if the user is using a device below
     * version 23
     * @param permission
     * @return
     */
    private boolean hasPermission(String permission) {
        if (isMarshorAbove()) {
            return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }
    /**
     * Check if device is Android Marshmallow or later (version 23)
     * @return
     */
    private boolean isMarshorAbove() {
        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
