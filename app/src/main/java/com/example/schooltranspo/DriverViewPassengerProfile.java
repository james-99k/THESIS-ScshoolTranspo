package com.example.schooltranspo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DriverViewPassengerProfile extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    ImageView passengerProfPic;
    TextView fName, email, contact, eContact, gender, address, sAddress, schoolName;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String passID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_passenger_profile);

        Toolbar toolbar = findViewById(R.id.passengerProfile);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Passenger Profile");
            actionBar.show();
        }

        Intent intent = getIntent();
        passID = intent.getStringExtra("PassengerName");

        passengerProfPic = findViewById(R.id.ibPassengerViewProfile);

        fName = findViewById(R.id.tvPassengerViewProfileFullName);
        email = findViewById(R.id.tvPassengerViewProfileEmail);
        contact = findViewById(R.id.tvPassengerViewProfileContact);
        eContact = findViewById(R.id.tvPassengerViewProfileEmergencyContact);
        gender = findViewById(R.id.tvPassengerViewProfileGender);
        address = findViewById(R.id.tvPassengerViewProfileAddress);
        sAddress = findViewById(R.id.tvPassengerViewProfileSchoolAddress);
        schoolName = findViewById(R.id.tvPassengerViewProfileSchoolName);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        StorageReference profileRef = storageReference.child("Users/"+passID+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(passengerProfPic);
            }
        });

        CollectionReference docRef = firebaseFirestore.collection("Users");

        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                    String passengerID = qdSnapshot.getId();
                    if(passengerID.equals(passID)) {
                        fName.setText(qdSnapshot.getString("FullName"));
                        email.setText(qdSnapshot.getString("Email"));
                        contact.setText(qdSnapshot.getString("Contact"));
                        eContact.setText(qdSnapshot.getString("EmergencyContact"));
                        gender.setText(qdSnapshot.getString("Gender"));
                        address.setText(qdSnapshot.getString("Address"));
                        sAddress.setText(qdSnapshot.getString("SchoolAddress"));
                    }
                }
            }
        });
    }
}