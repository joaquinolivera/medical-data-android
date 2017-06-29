package com.example.ana.exampleapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

import static android.content.ContentValues.TAG;


public class GpsService extends Service implements LocationListener {


    private static Location nLocation;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        gpsLocationManager();
        try {
            LocationDataSend runner = new LocationDataSend();
            runner.execute(this);
        } catch (Exception e) {
            Log.e(TAG,String.format("Error en ejecucion del servicio GpsService %s", e.getMessage()),e);
        }
        stopSelf();
    }


    public static Location getnLocation() {
        return nLocation;
    }

    public static void setnLocation(Location nLocation) {
        GpsService.nLocation = nLocation;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        setnLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Not needed in application structure
    }

    @Override
    public void onProviderEnabled(String s) {
        // Not needed in application structure
    }

    @Override
    public void onProviderDisabled(String s) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void gpsLocationManager() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        setnLocation(getLastKnownLocation());
    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
