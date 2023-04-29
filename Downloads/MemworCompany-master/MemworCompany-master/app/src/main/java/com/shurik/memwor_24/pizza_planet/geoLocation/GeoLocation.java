package com.shurik.memwor_24.pizza_planet.geoLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

// класс геолокации
public class GeoLocation {

    private Context context;

    public GeoLocation(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public Location getUserLocation() {
        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.
                getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }
}