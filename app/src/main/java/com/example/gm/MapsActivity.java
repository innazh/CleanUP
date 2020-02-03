package com.example.gm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import java.io.FileOutputStream;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    Button cameraBtn;
    public static final int CAMERA_REQUEST=2000;
    //for new post
    String newTitle=null;
    LatLng newLatLng;
    String newDesc;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        //Screen FULL
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen


        setContentView(R.layout.activity_maps);

        //Get Location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Get map fragment by id
        cameraBtn = findViewById(R.id.cameraBtn);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        //Once we come back here:
        Intent intent = this.getIntent();

        /* Obtain String from Intent  */
        if(intent !=null)
        {
            newDesc = intent.getStringExtra("desc");
            newTitle = intent.getStringExtra("title");
            newLatLng = intent.getParcelableExtra("loc");

//            System.out.println("AAAAAAAAAA" + desc + title + loc);
        }


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CAMERA");
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isInfoWindowShown()){
                    System.out.println("YOOOOOOO");
                }
                else marker.showInfoWindow();
                return true;
            }
        });


        // Add a marker in Sydney and move the camera
        LatLng toronto = new LatLng(43.773745, -79.500364);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 15));

        //Add markers for garbage spots
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.767404, -79.499301)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title("In Progress").snippet("4 people"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.766242,  -79.505061)).title("Needs Clean up").snippet("3/10"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.766424, -79.498592)).title("Needs Clean up").snippet("5/10"));

        if(newTitle!=null) {
            //NEW POST
            Marker m = mMap.addMarker(new MarkerOptions().position(newLatLng).title(newTitle).snippet(newDesc));
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(curr).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        try{
            Location l;
            l = locationManager.getLastKnownLocation(provider);
            LatLng curr = new LatLng(l.getLatitude(), l.getLongitude());
            mMap.addMarker(new MarkerOptions().position(curr).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
        }
        catch(SecurityException e) {}

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            System.out.println(bitmap);

            try {
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                bitmap.recycle();

                //Pop intent
                Intent intent = new Intent(MapsActivity.this, FullscreenActivity.class);
                intent.putExtra("image", filename);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */