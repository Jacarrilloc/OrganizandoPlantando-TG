package com.example.opcv.business.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class GeocodeInputAsyncTask extends AsyncTask<Void, Void, GeoPoint> {
    private WeakReference<Context> contextRef;
    private String addressStr;
    private Marker startMarker;
    private MapView map;

    public GeocodeInputAsyncTask(Context context, String addressStr, Marker startMarker, MapView map) {
        this.contextRef = new WeakReference<>(context);
        this.addressStr = addressStr;
        this.startMarker = startMarker;
        this.map = map;
    }

    @Override
    protected GeoPoint doInBackground(Void... voids) {
        Context context = contextRef.get();
        if (context != null) {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(addressStr, 1);
                if (addresses == null || addresses.size() == 0) {
                    return null;
                } else {
                    Address address = addresses.get(0);
                    return new GeoPoint(address.getLatitude(), address.getLongitude());
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(GeoPoint point) {
        Context context = contextRef.get();
        if (context != null) {
            if (point != null) {
                startMarker.setPosition(point);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);
                map.getController().animateTo(point);
            } else {
                Toast.makeText(context, "No location found for the entered address.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
