package com.example.asterisk.googlemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asterisk.googlemap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText etsearchbar;
    Button btngo, btnsatellite, btnnormal, btnterrain;
    LatLng clicklatlan;
    GoogleMap map;
    RequestQueue requestQueue;
    String incompleteURL="http://maps.googleapis.com/maps/api/geocode/json?address=";
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etsearchbar = findViewById(R.id.searchbar);
        btngo = findViewById(R.id.go);
        btnsatellite = findViewById(R.id.satelite);
        btnnormal = findViewById(R.id.normal);
        btnterrain = findViewById(R.id.terrain);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        LatLng l = new LatLng(27.6853, 85.3743);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
        googleMap.addMarker(new MarkerOptions().position(l).title("Marked Item"));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clicklatlan = latLng;
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                PolylineOptions options = new PolylineOptions();
                options.add(clicklatlan);
                options.add(latLng);
                googleMap.addPolyline(options);
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            }
        });

        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = etsearchbar.getText().toString();
                address = address.replace(" ","_");
                String search = incompleteURL + address;
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest sr = new StringRequest(Request.Method.GET, search, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            JSONArray ar1 = obj1.getJSONArray("results");
                            JSONObject obj2 = ar1.getJSONObject(0);
                            JSONObject obj3 = obj2.getJSONObject("geometry");
                            JSONObject obj4 = obj3.getJSONObject("location");
                            Double lat = obj4.getDouble("lat");
                            Double lng = obj4.getDouble("lng");
                            LatLng l = new LatLng(lat, lng);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(l,15));
                            map.addMarker(new MarkerOptions().position(l).title("Marked item"));
                            } catch (Exception e) {
                            }
                            }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        }
                        });
                requestQueue.add(sr);
                }
                });
            btnnormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }});
            btnsatellite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE); }
                });
            btnterrain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN); }
                });
            }}