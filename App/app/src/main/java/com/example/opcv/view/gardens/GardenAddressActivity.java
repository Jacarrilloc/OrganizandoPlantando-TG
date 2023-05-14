package com.example.opcv.view.gardens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.opcv.R;
import com.example.opcv.business.maps.GeocodeAsyncTask;
import com.example.opcv.business.maps.GeocodeInputAsyncTask;
import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.model.persistance.firebase.GardenCommunication;
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

        GardenCommunication gardenCommunication = new GardenCommunication();
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage("Para lo siguiente es necesario activar su ubicación. Oprima siguiente para activar")
                    .setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);
        map.setMultiTouchControls(true);

        // Create a marker
        final Marker startMarker = new Marker(map);

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            recreate();
        } else {
            // Add MyLocationNewOverlay
            MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
            myLocationOverlay.enableMyLocation();
            map.getOverlays().add(myLocationOverlay);

            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);

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
                gardenCommunication.addGardenAddress(idGarden, addressStr, point);
                Notifications notifications = new Notifications();
                notifications.notification("Huerta creada", "Felicidades! Tu huerta ha sido creada.", GardenAddressActivity.this);
                startActivity(new Intent(GardenAddressActivity.this, HomeActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso fue concedido. Reiniciar la actividad para que la ubicación se pueda obtener inmediatamente.
                recreate();
            } else {
                // El permiso fue denegado. Manejar este caso si es necesario.
            }
        }
    }


    /*
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Para lo siguiente se necesitan permisos de ubicación. Oprima siguiente para otorgarlos")
                .setCancelable(false)
                .setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }*/

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