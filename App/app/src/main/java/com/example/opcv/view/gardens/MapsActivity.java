package com.example.opcv.view.gardens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.Marker;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;

public class MapsActivity extends AppCompatActivity {
    private MapView map;
    private Button profile, rewards, home, ludification;
    private MapController myMapController;
    private ImageView gardens;
    GeoPoint bogota = new GeoPoint(4.62, -74.07);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_maps);

        profile = (Button) findViewById(R.id.profile);
        rewards = (Button) findViewById(R.id.rewards);
        home = (Button) findViewById(R.id.myGardens);
        gardens = (ImageView) findViewById(R.id.gardensIcon);
        ludification = (Button) findViewById(R.id.ludification);
        map =findViewById(R.id.mapglobal);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        Marker marker = new Marker(map);
        marker.setTitle("Mi Marcador");
        Drawable myIcon = getResources().getDrawable(R.drawable.dr_location_red, this.getTheme());
        marker.setIcon(myIcon);
        marker.setPosition(this.bogota);
        marker.setAnchor(Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, EditUserActivity.class));
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, RewardHomeActivity.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
            }
        });

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, GardensAvailableActivity.class));
            }
        });



        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(MapsActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(this.bogota);
    }
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        final android.content.res.Configuration override = new android.content.res.Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
        super.attachBaseContext(newBase);
    }
    @Override
    public void onConfigurationChanged(@NonNull android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        android.content.res.Configuration config = new android.content.res.Configuration(newConfig);
        adjustFontScale(getApplicationContext(), config);
    }
    public static void adjustFontScale(Context context, android.content.res.Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }
}