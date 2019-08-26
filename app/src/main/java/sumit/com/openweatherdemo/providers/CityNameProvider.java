package sumit.com.openweatherdemo.providers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import sumit.com.openweatherdemo.Exceptions.PermissionNotGrantedException;
import sumit.com.openweatherdemo.Exceptions.ProviderDisabledException;
import sumit.com.openweatherdemo.Exceptions.ProviderNotEnabledException;

public class CityNameProvider {

    private Context mContext;
    LocationManager mLocationManager;


    @Inject
    public CityNameProvider(Context context) {
        mContext = context;
        mLocationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * returns city name.
     * @return  Single<String>  Single of type String that is city name.
     */
    @SuppressWarnings({"MissingPermission"})
    public Single<String> getCity() {

        return Single.create(emitter -> {
            if (!isLocationPermissionGranted()) {
                emitter.onError(new PermissionNotGrantedException("Permission not granted"));
                return;

            }
            if (!isGpsAndNetworksProviderEnabled()) {
                emitter.onError(new ProviderNotEnabledException("Enable setting"));
                return;
            }

            String provider = mLocationManager.getBestProvider(new Criteria(),true);
            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {
                emitter.onSuccess(findCityFromLocation(location));
                return;
            }


            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            String cityName = findCityFromLocation(location);
                            emitter.onSuccess(cityName);
                            mLocationManager.removeUpdates(this);

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            emitter.onError(new ProviderDisabledException("provider disabled"));

                        }
                    });

        });
    }


    private boolean isGpsAndNetworksProviderEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private String findCityFromLocation(Location location) {

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());


        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return  addresses.get(0).getLocality();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown location";
    }

}
