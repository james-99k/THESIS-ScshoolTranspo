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

public class PassengerCarpoolSearchViewActivity extends AppCompatActivity {

    ImageView vehicleProfile;
    TextView vehicleName, status, driver, schoolName, routs, timeFetch, fees, driverID;
    Button subscribe;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;

    String IDVehicle, operatorID;
    Subscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_carpool_search_view);

        Toolbar toolbar = findViewById(R.id.passengerVehicleTB);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Vehicle");
            actionBar.show();
        }
        Intent intent = getIntent();
        IDVehicle = intent.getStringExtra("vehicleID");
        operatorID = intent.getStringExtra("operatorID");

        vehicleProfile = findViewById(R.id.pcarpoolImage);
        vehicleName = findViewById(R.id.pvehicleBrand);
        status = findViewById(R.id.pstatusCarpool);
        driver = findViewById(R.id.pdriverAssign);
        schoolName = findViewById(R.id.pcarpoolSchoolDestination);
        routs = findViewById(R.id.pcarpoolRouts);
        timeFetch = findViewById(R.id.pcarpoolTimeFetch);
        fees = findViewById(R.id.pcarpoolFees);
        driverID = findViewById(R.id.driverID);

        subscribe = findViewById(R.id.subCarpoolBtn);

        subscriber = new Subscriber();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String userID = user.getUid();

        System.out.println(userID);

        Query query = firebaseDatabase.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("vehicleID").getValue().toString().equals(IDVehicle) && !dataSnapshot.child("driverName").getValue().toString().equals("No Assign Driver")) {
                        driver.setText(dataSnapshot.child("driverName").getValue().toString());
                        vehicleName.setText(dataSnapshot.child("vehicles").getValue().toString());

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

        CollectionReference docRef = firebaseFirestore.collection("Users");
        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                    if(qdSnapshot.getString("UserType").equals("Driver") && qdSnapshot.getString("FullName").equals(driver.getText().toString())) {
                        driverID.setText(qdSnapshot.getId());
                        System.out.println(driverID.getText().toString());
                    }
                }
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverName = driver.getText().toString();
                String dID = driverID.getText().toString();
                String vehicleBrand = vehicleName.getText().toString();
                //System.out.println(driverID.getText().toString());
                subscribeData(operatorID, IDVehicle, driverName, userID, vehicleBrand, dID);
                driverID.setText("");
//                Toast.makeText(PassengerCarpoolSearchViewActivity.this, "Subscribed, Please wait for Confirmation", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void subscribeData(String oID, String vID, String driver, String uID, String Vehicle, String dID) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("Subscriber");

        String Status = "Waiting for Confirmation";

        Query query = firebaseDatabase.child("Subscriber");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(uID) && dataSnapshot.child("status").getValue().equals("Subscribed")) {
                        System.out.println(uID);
                        System.out.println(dataSnapshot.getKey());
//                       Toast.makeText(PassengerCarpoolSearchViewActivity.this, "Please unsubscribe your subscription", Toast.LENGTH_SHORT).show();

                        //Toast.makeText(PassengerCarpoolSearchViewActivity.this, "Subscribed, Please wait for Confirmation", Toast.LENGTH_SHORT).show();
                        x++;
                    }
//                    else {
//                        Toast.makeText(PassengerCarpoolSearchViewActivity.this, "Please unsubscribe your subscription", Toast.LENGTH_SHORT).show();
//                    }
                }
                if(x>0) {
                    Toast.makeText(PassengerCarpoolSearchViewActivity.this, "Please unsubscribe your subscription first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    subscriber.setOperatorID(oID);
                    subscriber.setVehicleID(vID);
                    subscriber.setDriverID(dID);
                    subscriber.setDriverName(driver);
                    subscriber.setPassengerID(uID);
                    subscriber.setStatus(Status);
                    subscriber.setVehicles(Vehicle);

                    rootRef.child(uID).setValue(subscriber);
                    Toast.makeText(PassengerCarpoolSearchViewActivity.this, "Subscribed, Please wait for Confirmation", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}