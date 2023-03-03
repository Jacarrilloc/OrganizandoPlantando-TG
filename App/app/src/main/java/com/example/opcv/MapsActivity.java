package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.Marker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MapsActivity extends AppCompatActivity {
    private MapView map;
    private Button profile, myGardens, gardensMap;
    private MapController myMapController;
    GeoPoint bogota = new GeoPoint(4.62, -74.07);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_maps);
        map = findViewById(R.id.mapglobal);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        Marker marker = new Marker(map);
        marker.setTitle("Mi marcador");
        Drawable myIcon = getResources().getDrawable(R.drawable.dr_location_red, this.getTheme());
        marker.setIcon(myIcon);
        marker.setPosition(this.bogota);
        marker.setAnchor(Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);


        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.globalMap);

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        IMapController mapController = map.getController();
        mapController.setZoom(20.0);
        mapController.setCenter(this.bogota);
    }
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}