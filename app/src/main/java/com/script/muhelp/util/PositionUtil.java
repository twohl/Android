package com.script.muhelp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.util.List;

import static com.script.muhelp.VarPool.*;

/**
 * Created by hongl on 2018/2/23.
 */

public class PositionUtil {
    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private static String locationProvider = null;
    private static Activity context;

    public static void setLocationListener(LocationListener locationListener) {
        PositionUtil.locationListener = locationListener;
    }

    public static Location tryLocation(Activity activity) {
        context = activity;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getAllProviders();
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {

            return null;
        }
        Location location = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationProvider, 1000, 1,locationListener);
            location = locationManager.getLastKnownLocation(locationProvider);
        }else{
            ActivityCompat.requestPermissions(context,PERMISSION_LIST,PERMISSION);
        }
        return location;
    }
    public static Location getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationProvider, 1000, 1, locationListener);
        }
        return locationManager.getLastKnownLocation(locationProvider);
    }
}
