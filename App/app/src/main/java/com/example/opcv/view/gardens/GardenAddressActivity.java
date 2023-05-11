package com.example.opcv.view.gardens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.opcv.R;
import com.example.opcv.business.maps.GeocodeAsyncTask;
import com.example.opcv.business.maps.GeocodeInputAsyncTask;
import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.model.persistance.garden.GardenPersistance;
import com.example.opcv.view.base.HomeActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class GardenAddressActivity extends AppCompatActivity {

    private MapView map;
    public EditText address;
    private Button show, next;
    private String idGarden;
    private GeoPoint point;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_garden_address);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idGarden= getIntent().getStringExtra("idGarden");
        }

        map = findViewById(R.id.mapglobal);
        address = findViewById(R.id.addressinput);
        show = findViewById(R.id.showButton);
        next = findViewById(R.id.nextButton);

        GardenPersistance gardenPersistance = new GardenPersistance();

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);
        map.setMultiTouchControls(true);

        // Create a marker
        final Marker startMarker = new Marker(map);

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        } else {
            // Add MyLocationNewOverlay
            MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
            myLocationOverlay.enableMyLocation();
            map.getOverlays().add(myLocationOverlay);

            myLocationOverlay.runOnFirstFix(new Runnable() {
                public void run() {
                    if (myLocationOverlay.getMyLocation()!=null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                map.getController().animateTo(myLocationOverlay.getMyLocation());
                            }
                        });
                    }
                }
            });

            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);

            // Add MapEventsOverlay
            MapEventsReceiver mReceive = new MapEventsReceiver() {
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {
                    // Update the position of the marker
                    point = p;
                    startMarker.setPosition(p);
                    new GeocodeAsyncTask(GardenAddressActivity.this, p).execute();
                    map.invalidate();
                    return true;
                }

                @Override
                public boolean longPressHelper(GeoPoint p) {
                    return false;
                }
            };

            MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
            map.getOverlays().add(OverlayEvents);

        }

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the address entered by the user
                String addressStr = address.getText().toString();
                new GeocodeInputAsyncTask(GardenAddressActivity.this, addressStr,  startMarker, map).execute();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressStr = address.getText().toString();
                gardenPersistance.addGardenAddress(idGarden, addressStr, point);
                Notifications notifications = new Notifications();
                notifications.notification("Huerta creada", "Felicidades! Tu huerta ha sido creada.", GardenAddressActivity.this);
                startActivity(new Intent(GardenAddressActivity.this, HomeActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

}