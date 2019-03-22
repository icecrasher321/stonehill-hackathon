package com.mancj.example;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mancj.example.custom.WelcomeActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.support.design.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    MaterialSearchBar searchBar;
    private DrawerLayout drawer;

    boolean location_found;
    private FusedLocationProviderClient fusedLocationClient;
    double latitude;
    double longitude;
    String url = "http://192.168.113.208:8080/api/dish/";
    String preference;
    Geocoder geocoder;
    List<Address> addresses;
    boolean firstRun;
    Button try_new;
    Button try_old;
    String try_new_food;
    boolean try_checked;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstRun = getSharedPreferences("firstRun", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstRun) {
            getSharedPreferences("firstRun", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        }
        try_new=findViewById(R.id.try_new);
        try_old=findViewById(R.id.try_old);
        location_found = false;
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                location_found = true;
                                Log.d("MainActivity", "Latitude:" + location.getLatitude() + "Longitude:" + location.getLongitude());
                            }
                        }
                    });
        }
        catch (SecurityException e) {
        }

        try_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try_new_food="1";
                try_checked=true;
            }
        });

        try_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try_new_food="0";
                try_checked=true;
            }
        });

        if(location_found && try_checked){
            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                //HttpGet httpGet = new HttpGet("http://192.168.113.208:8080/api/dish/");
                HttpPut httpPut = new HttpPut("http://192.168.113.208:8080/api/user/");

                List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);
                parameters.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
                parameters.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
                parameters.add(new BasicNameValuePair("preference_new", try_new_food));
                httpPut.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));

                HttpResponse response = httpClient.execute(httpPut);
                HttpEntity entity = response.getEntity();
            }
            catch(Exception c){}
        }


        try_new=findViewById(R.id.try_new);
        preference="";
        location_found = false;
        /*try_new=findViewById(R.id.try_new);
        try_old=findViewById(R.id.try_old);
        try_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preference="new";
            }
        });
        try_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preference="old";
            }
        });
        Log.d("MainActivity","hello to satvik");
        geocoder = new Geocoder(this, Locale.getDefault());*/




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
        searchBar.inflateMenu(R.menu.main);
        Log.d("LOG_TAG", "search text:"+ searchBar.getText());
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }
    }

}
