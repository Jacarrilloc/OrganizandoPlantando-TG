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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.business.interfaces.firebase.map.GetGardensAddressesInt;
import com.example.opcv.business.maps.GardenMaps;
import com.example.opcv.model.entity.Address;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.view.auth.SignOffActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity {
    private MapView map;
    private Button profile, rewards, home, ludification;
    private FloatingActionButton back;
    private ImageView gardens;
    GeoPoint bogota = new GeoPoint(4.62, -74.07);

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_maps);

        profile = (Button) findViewById(R.id.profile);
        rewards = (Button) findViewById(R.id.rewards);
        home = (Button) findViewById(R.id.myGardens);
        ludification = (Button) findViewById(R.id.ludification);
        back = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        map =findViewById(R.id.mapglobal);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

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

        GardenMaps gardenMaps = new GardenMaps();
        gardenMaps.getAddresses(new GetGardensAddressesInt() {
            @Override
            public void onComplete(Map<Integer, Address> addresses) {
                for (int i = 0; i < addresses.size(); i++){
                    System.out.println("eee: "+ Objects.requireNonNull(addresses.get(i)).getGardenName());

                    Marker marker = new Marker(map);
                    marker.setTitle(Objects.requireNonNull(addresses.get(i)).getGardenName());
                    marker.setPosition(Objects.requireNonNull(addresses.get(i)).getPoint());
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(marker);

                }
            }

        });

        AuthCommunication authCommunication = new AuthCommunication();
        FirebaseUser user = authCommunication.guestUser();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(MapsActivity.this, EditUserActivity.class));
                }
                else{
                    startActivity(new Intent(MapsActivity.this, SignOffActivity.class));
                }
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(MapsActivity.this, RewardHomeActivity.class));
                }
                else{
                    Toast.makeText(MapsActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, HomeActivity.class));
            }
        });



        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(MapsActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(MapsActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}