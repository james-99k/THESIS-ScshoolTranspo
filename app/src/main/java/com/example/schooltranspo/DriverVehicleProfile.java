package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DriverVehicleProfile extends AppCompatActivity {

    TextView brand, model, color, plateNo, capacity, schoolName, schoolDestination, serviceFee, operatorName;
    Button viewOperator;
    String fName;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_vehicle_profile);

        Toolbar toolbar = findViewById(R.id.driverViewVehicle);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Carpool Info");
            actionBar.show();
        }

        Intent intent = getIntent();
        fName = intent.getStringExtra("FullName");

        operatorName = findViewById(R.id.operatorName);
        brand = findViewById(R.id.drVehicleBrand);
        model = findViewById(R.id.drVehicleModel);
        color = findViewById(R.id.drVehicleColor);
        plateNo = findViewById(R.id.drVehiclePlateNo);
        capacity = findViewById(R.id.drVehicleCapacity);
        schoolName = findViewById(R.id.drVehicleSchoolName);
        schoolDestination = findViewById(R.id.drVehicleSchoolDestination);
        serviceFee = findViewById(R.id.drVehicleServiceFee);
        viewOperator = findViewById(R.id.viewOperatorInfo);

        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = databaseReference.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("driverName").getValue().toString().equals(fName)) {
                        String vehicleID = dataSnapshot.getKey();

                        CollectionReference cf = firebaseFirestore.collection("Vehicles");

                        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                    if(qdSnapshot.getId().equals(vehicleID)) {
                                        brand.setText(qdSnapshot.getString("Brand"));
                                        model.setText(qdSnapshot.getString("Model"));
                                        color.setText(qdSnapshot.getString("Color"));
                                        plateNo.setText(qdSnapshot.getString("PlateNumber"));
                                        capacity.setText(qdSnapshot.getString("Capacity"));
                                        schoolName.setText(qdSnapshot.getString("SchoolName"));
                                        schoolDestination.setText(qdSnapshot.getString("SchoolDestination"));
                                        serviceFee.setText(qdSnapshot.getString("ServiceFee"));

                                        String operatorID = qdSnapshot.getString("Operator ID");

                                        CollectionReference cf2 = firebaseFirestore.collection("Users");
                                        cf2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                                    if(qdSnapshot.getId().equals(operatorID)) {
                                                        operatorName.setText(qdSnapshot.getString("FullName"));
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DriverViewOperatorInfo.class);
                intent.putExtra("DriverName", fName);
                startActivity(intent);
            }
        });
    }
}