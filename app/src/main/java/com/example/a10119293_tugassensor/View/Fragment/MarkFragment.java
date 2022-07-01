package com.example.a10119293_tugassensor.View.Fragment;

//10119293
//Dwi Irfansyah
//IF-7

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.a10119293_tugassensor.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MarkFragment extends Fragment {

    FusedLocationProviderClient client;
    private GoogleMap map;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng Marker1 = new LatLng(-6.887012898143729, 107.61509505792367);
    LatLng Marker2 = new LatLng(-6.888549316302707, 107.61311778545742);
    LatLng Market3 = new LatLng(-6.900748117237799, 107.61617318158656);
    LatLng Marker4 = new LatLng(-6.891808676629883, 107.61680847668764);
    LatLng Marker5 = new LatLng(-6.883676085030079, 107.61503384258239);


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        arrayList.add(Marker1);
        arrayList.add(Marker2);
        arrayList.add(Market3);
        arrayList.add(Marker4);
        arrayList.add(Marker5);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                map = googleMap;
                map.addMarker(new MarkerOptions().position(Marker1).title("Richeese Factory Dipatiukur"));
                map.addMarker(new MarkerOptions().position(Marker2).title("MIE GACOAN DAGO"));
                map.addMarker(new MarkerOptions().position(Market3).title("Nasi Bancakan Wassalam Abah Barna"));
                map.addMarker(new MarkerOptions().position(Marker4).title("Bebek Ali Borme"));
                map.addMarker(new MarkerOptions().position(Marker5).title("Ayam Geprek Kegeprek Tubagus"));
                for (int i = 0; i < arrayList.size(); i++) {
                    map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    map.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){

        SupportMapFragment mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(
                    task -> {
                        Location location = task.getResult();
                        if (location != null) {
                            mapFragment.getMapAsync(googleMap -> {
                                LatLng lokasi = new LatLng(location.getLatitude(),location.getLongitude());
                                MarkerOptions options = new MarkerOptions().position(lokasi).title("Lokasi Anda Saat Ini");
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasi,17));
                                googleMap.addMarker(options);
                            });
                        }
                        else {
                            LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void
                                onLocationResult(@NonNull LocationResult locationResult)
                                {
                                    mapFragment.getMapAsync(googleMap -> {
                                        LatLng lokasi = new LatLng(location.getLatitude(),location.getLongitude());
                                        MarkerOptions options = new MarkerOptions().position(lokasi).title("Lokasi Sekarang");
                                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasi,17));
                                        googleMap.addMarker(options);
                                    });
                                }
                            };

                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    });
        }
        else {
            startActivity(
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}