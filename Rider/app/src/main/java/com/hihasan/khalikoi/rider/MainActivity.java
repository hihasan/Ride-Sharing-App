package com.hihasan.khalikoi.rider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hihasan.khalikoi.rider.help.Help;
import com.hihasan.khalikoi.rider.settings.Settings;
import com.hihasan.khalikoi.rider.user.profile.ProfileMain;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,LocationEngineListener,PermissionsListener,MapboxMap.OnMapClickListener {

    private Context context=this;
    private AppCompatButton fab_button,yes_signout,yes_exit,no_signout,no_exit;
    private TextView edit_profile;

    //MapBox Value
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private AppCompatEditText current_location, destination;
    private AppCompatButton submit_search;

    //Pointer Position
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;

    //Navigation Route Variables
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG="MainActivity";

    //Dummy Assets
    private AppCompatButton startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaGloYXNhbiIsImEiOiJjamt5bGJjc2owa21oM2twNHpveTl6dmViIn0.P-c22QlXEDcGUc7RC77m-Q");
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (destinationMarker !=null){
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    NavigationLauncherOptions options= NavigationLauncherOptions.builder()
                            .origin(originPosition)
                            .destination(destinationPosition)
                            .shouldSimulateRoute(true)
                            .build();

                    NavigationLauncher.startNavigation(MainActivity.this,options);

                    //Methods Will be implemented Here

                }

                else {
                    Toast.makeText(getApplicationContext(),"Please Select The Destination",Toast.LENGTH_SHORT).show();
                }

                //I WIll Develop it latter

            }
        });

//        edit_profile=(TextView) findViewById (R.id.edit_profile);
//        edit_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent n=new Intent(MainActivity.this, ProfileMain.class);
//                startActivity(n);
//            }
//        });

        //Drawer Layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //OnMap Ready Callback
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map=mapboxMap;
        map.addOnMapClickListener(this);
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

    //OnMap Click Function
    @Override
    public void onMapClick(@NonNull LatLng point) {

        if (destinationMarker !=null){
            map.removeMarker(destinationMarker);
        }
        //Here  I can Change Icon, I can add Title
        destinationMarker=map.addMarker(new MarkerOptions().position(point));

        destinationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition=Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
        getRoute(originPosition,destinationPosition);

        //Wrong Code
//        CoordinatorLayout.LayoutParams p=(CoordinatorLayout.LayoutParams) fab_button.getLayoutParams();
//        p.setAnchorId(R.id.fab);
//        fab_button.setLayoutParams(p);
//        fab_button.setVisibility(View.VISIBLE);

    }

    //function to get our route
    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body()==null){
                            Log.e(TAG,"No Routes Found, Check users & Access Token");
                            return;
                        }

                        else if (response.body().routes().size()==0){
                            Log.e(TAG,"No Routes Found");
                            Toast.makeText(getApplicationContext(),"No Route Found",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DirectionsRoute currentRoute=response.body().routes().get(0);

                        if (navigationMapRoute !=null){
                            navigationMapRoute.removeRoute();
                        }else{
                            navigationMapRoute=new NavigationMapRoute(null,mapView,map);
                        }

                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"ERROR: "+t.getMessage());
                    }
                });

    }

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_profile){
            Intent i=new Intent(MainActivity.this, ProfileMain.class);
            startActivity(i);
        }

        //Trips Details
        else if (id == R.id.nav_trips) {
            // Handle the camera action
        }

        //Help Details
        else if (id == R.id.nav_help) {
            Toast.makeText(getApplicationContext(),"This will Open Chatbox",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(MainActivity.this, Help.class);
            startActivity(i);

        }

        //PAyment Details
        else if (id == R.id.nav_payment) {

        }

        //Settings Details
        else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(),"This Will Open Settings",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(MainActivity.this, Settings.class);
            startActivity(i);

        }

        //Sign Out Activity
        else if (id == R.id.nav_sign) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_signout);
            dialog.setTitle("Title....");

            yes_signout= dialog.findViewById (R.id.yes);
            no_signout= dialog.findViewById (R.id.no);

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

            yes_exit= dialog.findViewById (R.id.yes);
            no_exit= dialog.findViewById (R.id.no);

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


            fab_button = dialog.findViewById(R.id.close);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}