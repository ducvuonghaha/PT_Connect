package com.tanxe.supple_online.helper;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

public class LocationUtils {
    private final long MIN_TIME = 500; // milliseconds
    private final float MIN_DISTANCE = 20; // meters

    private static LocationUtils instance = new LocationUtils();

    public static LocationUtils getInstance() {
        return instance;
    }

    private LocationListener locationListener;

    public boolean isEnableLocationService(Context context) {
        if (context == null) return false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return false;
        boolean gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gpsEnable || networkEnable;
    }

    public void requestUpdateLocation(Context context, LocationListener locationListener) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return;
        if (this.locationListener != null) locationManager.removeUpdates(this.locationListener);
        int permissionLocationNetwork = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (networkEnable && permissionLocationNetwork == PackageManager.PERMISSION_GRANTED) {
            this.locationListener = locationListener;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this.locationListener);
            return;
        }

        int permissionLocationGPS = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEnable && permissionLocationGPS == PackageManager.PERMISSION_GRANTED) {
            this.locationListener = locationListener;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this.locationListener);
        }
    }

    public void removeUpdateLocation(Context context) {
        if (context == null) return;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return;
        if (this.locationListener != null) locationManager.removeUpdates(this.locationListener);
        this.locationListener = null;
    }
}