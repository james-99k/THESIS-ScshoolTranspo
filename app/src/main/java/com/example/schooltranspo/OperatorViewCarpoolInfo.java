package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OperatorViewCarpoolInfo extends AppCompatActivity {

    TextView brand, model, color, plateNo, capacity, schoolName, schoolDestination, serviceFee;
    Button edit, delete;
    String vehicleID;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_view_carpool_info);

        Toolbar toolbar = findViewById(R.id.operatorViewCarpoolInfoToolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        vehicleID = intent.getStringExtra("vehicleID");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Carpool Info");
            actionBar.show();
        }

        brand = findViewById(R.id.inVehicleBrand);
        model = findViewById(R.id.inVehicleModel);
        color = findViewById(R.id.inVehicleColor);
        plateNo = findViewById(R.id.inVehiclePlateNo);
        capacity = findViewById(R.id.inVehicleCapacity);
        schoolName = findViewById(R.id.inVehicleSchoolName);
        schoolDestination = findViewById(R.id.inVehicleSchoolDestination);
        serviceFee = findViewById(R.id.inVehicleServiceFee);
        edit = findViewById(R.id.btnEditCarpool);
        delete = findViewById(R.id.btnDeleteCarpool);

        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
                    }
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditCarpoolInfo.class);
                intent.putExtra("vehicleID", vehicleID);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(OperatorViewCarpoolInfo.this)
                        .setTitle("Delete Carpool")
                        .setMessage("Do you want to delete this Carpool?")
                        .setPositiveButton("Delete", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button deleteButoon = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                deleteButoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference docRef = firebaseFirestore.collection("Vehicles").document(vehicleID);
                        docRef.delete();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("CarpoolList");
                        databaseReference.child(vehicleID).removeValue();
                        alertDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), OperatorHomepageActivity.class);
                        startActivity(intent);
                        Toast.makeText(OperatorViewCarpoolInfo.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}