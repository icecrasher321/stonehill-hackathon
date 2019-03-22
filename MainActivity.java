package com.mancj.example;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mancj.example.custom.WelcomeActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    MaterialSearchBar searchBar;
    private DrawerLayout drawer;

    boolean location_found;
    private FusedLocationProviderClient fusedLocationClient;
    double latitude;
    double longitude;
    String url = "http://httpbin.org/post";
    String preference;
    Button try_new;
    Button try_old;
    Geocoder geocoder;
    List<Address> addresses;
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstRun = getSharedPreferences("firstRun", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstRun) {
            getSharedPreferences("firstRun", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        }
        preference="";
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
        });*/

        geocoder = new Geocoder(this, Locale.getDefault());

        location_found = false;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                location_found=true;
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                            }
                        }
                    });
        }
        catch (SecurityException e) {
        }

        if (location_found && !preference.equals("")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("POST SUCCESS:", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("POST FAILURE:", "Error with the transaction");
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("coordinates", String.valueOf(latitude)+"-"+String.valueOf(longitude));
                    params.put("preference",preference);
                    return params;
                }
            };
            queue.add(postRequest);


            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i=0;i<response.length();i++){
                                try {
                                    JSONObject details=response.getJSONObject(i);
                                    String name=details.getString("name");
                                    String dishes=details.getString("menu");
                                    String price=details.getString("price");
                                    String address=details.getString("address");
                                    /*int latitude=details.getInt("latitude");
                                    int longitude=details.getInt("longitude");
                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    }
                                    catch(IOException c){}

                                    String address = addresses.get(0).getAddressLine(0);*/
                                }
                                catch(JSONException c){}
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("GET ERROR:", "Error with transaction");
                        }
                    }
            );
            queue.add(getRequest);
            queue.start();
        }

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
