package com.example.schooltranspo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class vehicleProfile extends AppCompatActivity {

    ImageView vehicleProfile;
    TextView vehicleName, status, driver, schoolName, routs, timeFetch, fees;
    Button viewCarpoolInfo, viewDriverInfo, removeDriver;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;

    String vehicleBrand, driverName, IDVehicle;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        Toolbar toolbar = findViewById(R.id.operatorToolbarAfter);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Vehicle");
            actionBar.show();
        }

        myDialog = new Dialog(this);

        Intent intent = getIntent();
        vehicleBrand = intent.getStringExtra("vehicleBrand");
        driverName = intent.getStringExtra("driverName");
        IDVehicle = intent.getStringExtra("vehicleID");

        vehicleProfile = findViewById(R.id.carpoolImage);
        vehicleName = findViewById(R.id.vehicleBrand);
        status = findViewById(R.id.statusCarpool);
        driver = findViewById(R.id.driverAssign);
        schoolName = findViewById(R.id.carpoolSchoolDestination);
        routs = findViewById(R.id.carpoolRouts);
        timeFetch = findViewById(R.id.carpoolTimeFetch);
        fees = findViewById(R.id.carpoolFees);
        viewCarpoolInfo = findViewById(R.id.viewCarpoolBtn);
        viewDriverInfo = findViewById(R.id.viewDriverBtn);
        removeDriver = findViewById(R.id.removeAssignDriver);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String OperatorID = user.getUid();

        Query query = firebaseDatabase.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("vehicleID").getValue().toString().equals(IDVehicle)) {
                        driver.setText(dataSnapshot.child("driverName").getValue().toString());
                        vehicleName.setText(vehicleBrand);

                        String vehicleID = dataSnapshot.getKey();

                        DocumentReference docRef = firebaseFirestore.collection("Vehicles").document(vehicleID);

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


        viewCarpoolInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OperatorViewCarpoolInfo.class);
                intent.putExtra("vehicleID", IDVehicle);
                startActivity(intent);
            }
        });

        viewDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!driver.getText().toString().equals("No Assign Driver")) {
                    Intent intent = new Intent(getApplicationContext(), OperatorViewDriverInfo.class);
                    intent.putExtra("driverName", driver.getText().toString());
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Assign driver First or If you assigned already, Please refresh this page by revisiting again", Toast.LENGTH_SHORT).show();
            }
        });

        removeDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operator operator = new Operator();
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("CarpoolList");

                if(!driver.getText().toString().equals("No Assign Driver")) {
                    String Status = "Not Operational";
                    String tempDriverName = "No Assign Driver";
                    operator.setDriverName(tempDriverName);
                    operator.setOperatorID(OperatorID);
                    operator.setVehicles(vehicleBrand);
                    operator.setVehicleID(IDVehicle);
                    firebaseDatabase.child(IDVehicle).setValue(operator);

//                    status.setText(Status);

                    CollectionReference cf = firebaseFirestore.collection("Vehicles");

                    cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                if (qdSnapshot.getId().equals(IDVehicle)) {
                                    DocumentReference df = firebaseFirestore.collection("Vehicles").document(IDVehicle);
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("Status", Status);
                                    df.update(userInfo);
                                }
                            }
                        }
                    });

                    Toast.makeText(getApplicationContext(), "Remove Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "You have not yet Assign a Driver", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowPopup(View view) {
        TextView brandCarpool;
        Spinner spinnerDriver;
        Button btnSave;
        final int[] ctr2 = {0};
        final int[] ctr3 = {0};

        Intent intent = getIntent();
        vehicleBrand = intent.getStringExtra("vehicleBrand");

        myDialog.setContentView(R.layout.assigndriverpopup);

        brandCarpool = myDialog.findViewById(R.id.brandCarpool);
        btnSave = myDialog.findViewById(R.id.saveAssignBtn);
        spinnerDriver = myDialog.findViewById(R.id.driverSpinner);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String OperatorID = user.getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference docRef = firebaseFirestore.collection("Users");
//        DocumentReference df = firebaseFirestore.document(OperatorID);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = firebaseDatabase.child("CarpoolList");

        ArrayList<String> dN = new ArrayList<>();
        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                    final int[] ctr = new int[1];
                    if(qdSnapshot.contains("Operator ID") && qdSnapshot.getString("Operator ID").equals(OperatorID)) {
                        String name = qdSnapshot.getString("FullName");

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (!name.equals(dataSnapshot.child("driverName").getValue().toString())) {
                                        ctr[0] = 1;
                                        if( ctr[0] == 1)
                                            ctr2[0] += 1;
                                    }
                                    else {
                                        ctr[0] = 0;
                                        if( ctr[0] == 0)
                                            ctr3[0] += 1;
                                    }
                                }
                                if(ctr2[0] >= 1 && ctr3[0] == 0) {
                                    dN.add(name);

                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dN);
                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDriver.setAdapter(spinnerAdapter);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
        });

        brandCarpool.setText(vehicleBrand);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("CarpoolList");
                Operator operator = new Operator();

                String Status = "Operational";
                operator.setDriverName(spinnerDriver.getSelectedItem().toString());
                operator.setVehicles(vehicleBrand);
                operator.setOperatorID(OperatorID);
                operator.setVehicleID(IDVehicle);

                firebaseDatabase.child(IDVehicle).setValue(operator);

                CollectionReference cf = firebaseFirestore.collection("Vehicles");

                cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                            if (qdSnapshot.getId().equals(IDVehicle)) {
                                DocumentReference df = firebaseFirestore.collection("Vehicles").document(IDVehicle);
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("Status", Status);
                                df.update(userInfo);
                            }
                        }
                    }
                });

                myDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Driver Assigned", Toast.LENGTH_SHORT).show();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}