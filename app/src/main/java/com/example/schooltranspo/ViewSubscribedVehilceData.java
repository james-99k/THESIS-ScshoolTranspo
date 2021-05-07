package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewSubscribedVehilceData extends AppCompatActivity {

    ImageView vehicleProfile;
    TextView vehicleName, status, driver, schoolName, routs, timeFetch, fees;
    Button unsubscribe, pay, feedback, viewCarpools;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference firebaseDatabase;

    String vName, driverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subscribed_vehilce_data);

        Toolbar toolbar = findViewById(R.id.passengerVehicleTB);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Carpool Details");
            actionBar.show();
        }
        Intent intent = getIntent();
        vName = intent.getStringExtra("vehicleBrand");
        driverName = intent.getStringExtra("driverName");

        vehicleProfile = findViewById(R.id.pcarpoolImage);
        vehicleName = findViewById(R.id.pvehicleBrand);
        status = findViewById(R.id.pstatusCarpool);
        driver = findViewById(R.id.pdriverAssign);
        schoolName = findViewById(R.id.pcarpoolSchoolDestination);
        routs = findViewById(R.id.pcarpoolRouts);
        timeFetch = findViewById(R.id.pcarpoolTimeFetch);
        fees = findViewById(R.id.pcarpoolFees);
        pay = findViewById(R.id.payBtn);
        unsubscribe = findViewById(R.id.unsubCarpoolBtn);
        feedback = findViewById(R.id.feedbackBtn);
        viewCarpools = findViewById(R.id.viewCarpoolBtn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        Query query = firebaseDatabase.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("driverName").getValue().toString().equals(driverName) && dataSnapshot.child("vehicles").getValue().toString().equals(vName)) {
                        driver.setText(dataSnapshot.child("driverName").getValue().toString());
                        vehicleName.setText(dataSnapshot.child("vehicles").getValue().toString());

                        String IDVehicle = dataSnapshot.child("vehicleID").getValue().toString();
                        DocumentReference docRef = firebaseFirestore.collection("Vehicles").document(IDVehicle);

                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String Status = documentSnapshot.getString("Status");
                                String SchoolN = documentSnapshot.getString("SchoolName");
                                String Fees = documentSnapshot.getString("ServiceFee");

                                status.setText(Status);
                                schoolName.setText(SchoolN);
                                fees.setText("P " + Fees + " Monthly");
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Payment.class);
                startActivity(intent);
            }
        });

        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewSubscribedVehilceData.this)
                        .setTitle("Unsubscribe?")
                        .setMessage("Are you sure to unsubscribe this Carpool?")
                        .setPositiveButton("Unsubscribe", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button unsubscribe = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                unsubscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Subscriber");

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("driverName").getValue().toString().equals(driverName) && dataSnapshot.child("vehicles").getValue().toString().equals(vName)) {
                                        String IDVehicle = dataSnapshot.child("vehicleID").getValue().toString();
                                        firebaseDatabase.child(IDVehicle).removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        alertDialog.dismiss();
                        Intent intent = new Intent(getApplication(), PassengerHomepageActivity.class);
                        startActivity(intent);
                        Toast.makeText(ViewSubscribedVehilceData.this, "Unsubscribed Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GiveFeedback.class);
                intent.putExtra("DriverName", driverName);
//                intent.putExtra("VehicleName", vName);
                startActivity(intent);
            }
        });

        viewCarpools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PassengerViewCarpoolList.class);
                startActivity(intent);
            }
        });
    }
}