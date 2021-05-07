package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DriverViewOperatorInfo extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    ImageView operatorProfPic;
    TextView fName, email, contact, address;
    FirebaseFirestore firebaseFirestore;
    String driverName;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_operator_info);

        Toolbar toolbar = findViewById(R.id.driverViewVehicle);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Carpool Info");
            actionBar.show();
        }

        Intent intent = getIntent();
        driverName = intent.getStringExtra("DriverName");

        operatorProfPic = findViewById(R.id.operatorProfile);

        fName = findViewById(R.id.tvOperatorViewProfileFullName);
        email = findViewById(R.id.tvOperatorViewProfileEmail);
        contact = findViewById(R.id.tvOperatorViewProfileContact);
        address = findViewById(R.id.tvOperatorViewProfileAddress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = databaseReference.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("driverName").getValue().toString().equals(driverName)) {
                        String operatorID = dataSnapshot.child("operatorID").getValue().toString();
                        CollectionReference cf = firebaseFirestore.collection("Users");

                        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                    if(qdSnapshot.getId().equals(operatorID)) {
                                        fName.setText(qdSnapshot.getString("FullName"));
                                        email.setText(qdSnapshot.getString("Email"));
                                        contact.setText(qdSnapshot.getString("Contact"));
                                        address.setText(qdSnapshot.getString("Address"));
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
    }
}