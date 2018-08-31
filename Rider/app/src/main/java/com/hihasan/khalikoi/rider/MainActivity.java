package com.hihasan.khalikoi.rider;
//
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,LocationEngineListener,PermissionsListener {

    private Context context=this;
    private AppCompatButton fab_button,yes_signout,yes_exit,no_signout,no_exit;

    //MapBox Value
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaGloYXNhbiIsImEiOiJjamt5bGJjc2owa21oM2twNHpveTl6dmViIn0.P-c22QlXEDcGUc7RC77m-Q");
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Drawer Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //OnMap Ready Callback
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map=mapboxMap;
        enableLocation();
    }



    private void enableLocation(){
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            //do something here later
            initializeLocationEngine();
            initializeLocationLayer();
        }
        else {
            permissionManager=new PermissionsManager(this);
            permissionManager.requestLocationPermissions(this);
        }

    }

    @SuppressLint("MissingPermission")
    private void initializeLocationEngine(){
        locationEngine=new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation=locationEngine.getLastLocation();
        if (lastLocation!=null){
            originLocation=lastLocation;
//            setCameraPosition(lastLocation);
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin=new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

//    private void setCameraPosition(Location location){
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13.0));
//    }

    //Location Engine Listener started
    @Override
    @SuppressLint("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location !=null){
            originLocation=location;
            //setCameraPosition(location);
        }
    }

    //Location Engine Lisenter End Here
    //Permission Listener Started
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Ned to call why permission needed
        Toast.makeText(getApplicationContext(),"Youneed permission To Open Maps ",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    //Android Activity Lifecycle
    @Override
    @SuppressWarnings("MissingPermission")
    public void onStart() {
        super.onStart();
        if (locationEngine !=null){
            locationEngine.requestLocationUpdates();
        }

        if (locationLayerPlugin !=null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine!=null){
            locationEngine.requestLocationUpdates();
        }

        if (locationLayerPlugin!=null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine !=null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Trips Details
        if (id == R.id.nav_trips) {
            // Handle the camera action
        }

        //Help Details
        else if (id == R.id.nav_help) {

        }

        //PAyment Details
        else if (id == R.id.nav_payment) {

        }

        //Settings Details
        else if (id == R.id.nav_settings) {

        }

        //Sign Out Activity
        else if (id == R.id.nav_sign) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_signout);
            dialog.setTitle("Title....");

            yes_signout=(AppCompatButton) dialog.findViewById (R.id.yes);
            no_signout=(AppCompatButton) dialog.findViewById (R.id.no);

            yes_signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"If User Loged in it will send them to SIgn In page",Toast.LENGTH_SHORT).show();
                   // System.exit(0);
                }
            });

            no_signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"No it will not exit", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        //Exit Activity
        else if (id == R.id.nav_exit) {

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_exit);
            dialog.setTitle("Title....");

            yes_exit=(AppCompatButton) dialog.findViewById (R.id.yes);
            no_exit=(AppCompatButton) dialog.findViewById (R.id.no);

            yes_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //need to Work on Here
                    System.exit(0);
                }
            });

            no_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"No it will not Sign Out", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        else if (id == R.id.nav_privacy) {
            Toast.makeText(getApplicationContext(),"This Will Open Privacy Policy Written By us",Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(),"About the Project",Toast.LENGTH_SHORT).show();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_about_us);
            dialog.setTitle("Title...");


            fab_button = (AppCompatButton) dialog.findViewById(R.id.close);
            fab_button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}