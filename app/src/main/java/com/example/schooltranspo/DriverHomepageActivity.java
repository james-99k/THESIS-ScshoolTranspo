package com.example.schooltranspo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class DriverHomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    PagerAdapter adapter;
    UserData ud = new UserData();
    TextView navDriverName;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;

    String locationID;

    private ArrayList<LatLng> passengerLatLng = new ArrayList<LatLng>();

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2000;
    private long FASTEST_INTERVAL = 3000;
    private LocationManager locationManager;
    private LatLng driverLatLng,pLatLng;
    private boolean isPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_homepage);
        Toolbar toolbar = findViewById(R.id.driverToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.driverDrawer);
        navigationView = findViewById(R.id.driverNavView);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        ud = (UserData)getIntent().getSerializableExtra("UD");

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();



        try{
            setNavigationViewListener();
        }
        catch (Exception e)
        {
            Log.e("TAG", "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            e.printStackTrace();
        }



        //Map Section
        if(requestSinglePermission()){

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation();
        }
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.driverNavView);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        navDriverName = header.findViewById(R.id.tvDriverNameNav);
        navDriverName.setText(ud.getFullName());

        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView passengerNavPic = findViewById(R.id.driverImageViewNav);
                Picasso.get().load(uri).into(passengerNavPic);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.driverViewProfile){
            Intent i = new Intent(getApplicationContext(), DriverViewProfileActivity.class);
            i.putExtra("UD",ud);
            startActivity(i);
        }
        if(item.getItemId() == R.id.driverMyVehicleProfile){
            String fName = ud.getFullName();
            Query query = databaseReference.child("CarpoolList");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean presence = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("driverName").getValue().toString().equals(fName)) {
                            Intent i = new Intent(getApplicationContext(), DriverVehicleProfile.class);
                            i.putExtra("FullName", fName);
                            startActivity(i);
                            presence = true;
                        }
                    }
                    if(!presence)
                        Toast.makeText(DriverHomepageActivity.this, "You don't have vehicles. Please contact Operator", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(item.getItemId() == R.id.driverLisOfPassengers){
            String fName = ud.getFullName();

            Query query = databaseReference.child("CarpoolList");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("driverName").getValue().toString().equals(fName)) {
                            Intent i = new Intent(getApplicationContext(), ListOfStudents.class);
                            i.putExtra("FullName", fName);
                            i.putExtra("OperatorID", dataSnapshot.child("operatorID").getValue().toString());
                            startActivity(i);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(item.getItemId() == R.id.driverFeedback){
            //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        if(item.getItemId() == R.id.driverLogout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


        if (mMap != null) {
            mMap.clear();

        }


        FirebaseUser user = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = firebaseFirestore.collection("Driver Location").document(user.getUid());
        Map<String, Object> driverLocation = new HashMap<>();
        driverLocation.put("Driver ID", user.getUid());
        driverLocation.put("Driver Name", ud.getFullName());
        driverLocation.put("Latitude", location.getLatitude());
        driverLocation.put("Longitude", location.getLongitude());
        documentReference.set(driverLocation);



        driverLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        checkPassengerSubscribed();

        if(driverLatLng!=null){

            mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Marker in Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLatLng,18));


        }

        for (int i=0; i < passengerLatLng.size();i++) {
            if (passengerLatLng!=null){

                mMap.addMarker(new MarkerOptions().position(passengerLatLng.get(i)).title("Passenger is here").icon(BitmapDescriptorFactory.fromResource(R.drawable.passengermarker)));

                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                bounds.include(driverLatLng).include(passengerLatLng.get(i));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds.build(), 150);
                mMap.moveCamera(cameraUpdate);
            }
        }

    }

    private boolean checkLocation() {

        if(!isLocationEnabled()){
            showAlert();
        }
        return isLocationEnabled();

    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }


                }).check();

        return isPermission;
    }

    private void startLocationUpdates() {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

    }
    @Override
    protected void onStart() {
        super.onStart();

        if(mGoogleApiClient !=null){
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

    }

    private void checkPassengerSubscribed(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        Query query = databaseReference.child("Subscriber");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> passengerID = new ArrayList<String>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    if (dataSnapshot.child("driverID").getValue().toString().equals(user.getUid())){

                        passengerID.add(dataSnapshot.child("passengerID").getValue().toString());

                        System.out.println(passengerID);


                        for (int i = 0 ; i < passengerID.size(); i++) {
                            DocumentReference docRef = firebaseFirestore.collection("Passenger Location").document(passengerID.get(i));

                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Double Latitude = documentSnapshot.getDouble("Latitude");
                                    Double Longitude = documentSnapshot.getDouble("Longitude");


                                    passengerLatLng.add(new LatLng(Latitude,Longitude));

                                    //    mMap.addMarker(new MarkerOptions().position(passengerLatLng.get(i)).title("Passenger is here").icon(BitmapDescriptorFactory.fromResource(R.drawable.passengermarker)));

                                }

                            });
                        }
                    }
                    else
                        Toast.makeText(DriverHomepageActivity.this,"No Passenger has subscribed to this driver",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}