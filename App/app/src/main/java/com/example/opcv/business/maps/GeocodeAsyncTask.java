package com.example.opcv.business.maps;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.opcv.view.gardens.GardenAddressActivity;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class GeocodeAsyncTask extends AsyncTask<Void, Void, String> {
    private WeakReference<GardenAddressActivity> activityRef;
    private GeoPoint point;

    public GeocodeAsyncTask(GardenAddressActivity activity, GeoPoint point) {
        this.activityRef = new WeakReference<>(activity);
        this.point = point;
    }

    @Override
    protected String doInBackground(Void... voids) {
        GardenAddressActivity activity = activityRef.get();
        if (activity == null) {
            return null;
        }

        try {
            Geocoder geocoder = new Geocoder(activity);
            List<Address> addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String addressString) {
        GardenAddressActivity activity = activityRef.get();
        if (activity == null || addressString == null) {
            return;
        }
        activity.address.setText(addressString);
    }

}

