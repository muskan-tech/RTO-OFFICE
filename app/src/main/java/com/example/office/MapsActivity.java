package com.example.office;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.office.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Marker mymarker;
    private DatabaseReference reference;
    LocationManager manager;
    String veh;
    private final int MIN_TIME=0;
    private final int MIN_DIST=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manager=(LocationManager)getSystemService(LOCATION_SERVICE);

        reference= FirebaseDatabase.getInstance().getReference().child("GJ059294");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getlocationupdate();
        Intent intent=new Intent() ;
        veh = intent.getStringExtra("vehicleNo");
    }
private void readchanges(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                try{
                if(snapshot.exists()){
                    Mylocation location=snapshot.getValue(Mylocation.class);
                    if(location !=null){
                        mymarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                        Geocoder geocoder =new Geocoder(getApplicationContext());
                        List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String str=address.get(0).getSubLocality();

//                        FirebaseFirestore.getInstance().collection(veh).update("location",str);
                    }

                }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
}
    private void getlocationupdate() {
        if(manager!=null){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            if(location!=null) {
                                saveLocation(location);

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Null Location",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            if(location!=null) {
                                saveLocation(location);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Null Location",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No provider available", Toast.LENGTH_SHORT).show();
                }
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults.length>0 && (grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                getlocationupdate();

            }
            else{
                Toast.makeText(getApplicationContext(),"Permission Required",Toast.LENGTH_SHORT).show();
            }
        }
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(21.1702, 72.8311);
        mymarker=mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Surat"));
        mMap.setMinZoomPreference(5);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



    private void saveLocation(Location location) {
        reference.setValue(location);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        saveLocation(location);
    }
}