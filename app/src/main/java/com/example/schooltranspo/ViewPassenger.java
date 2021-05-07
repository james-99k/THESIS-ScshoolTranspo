package com.example.schooltranspo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewPassenger extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    ImageView passengerProfPic;
    TextView fName, email, contact, eContact, gender, address, sAddress;
    Button confirm, refuse;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String passID, vehicleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_passenger);

        Toolbar toolbar = findViewById(R.id.operatorToolbarAfter);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Passenger");
            actionBar.show();
        }

        Intent intent = getIntent();
        passID = intent.getStringExtra("passID");
        vehicleID = intent.getStringExtra("vehicleID");

        passengerProfPic = findViewById(R.id.ibPassengerViewProfile);

        fName = findViewById(R.id.tvPassengerViewProfileFullName);
        email = findViewById(R.id.tvPassengerViewProfileEmail);
        contact = findViewById(R.id.tvPassengerViewProfileContact);
        eContact = findViewById(R.id.tvPassengerViewProfileEmergencyContact);
        gender = findViewById(R.id.tvPassengerViewProfileGender);
        address = findViewById(R.id.tvPassengerViewProfileAddress);
        sAddress = findViewById(R.id.tvPassengerViewProfileSchoolAddress);
        confirm = findViewById(R.id.btnConfirm);
        refuse = findViewById(R.id.refuseBtn);

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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewPassenger.this)
                        .setTitle("Confirm Passenger")
                        .setMessage("Do you want to Confirm this Passenger?")
                        .setPositiveButton("Confirm", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button Confirm = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Subscriber");
                        databaseReference.child(passID).child("status").setValue("Subscribed");
                        alertDialog.dismiss();
                        Intent intent = new Intent(getApplication(), OperatorHomepageActivity.class);
                        startActivity(intent);
                        Toast.makeText(ViewPassenger.this, "Confirmed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewPassenger.this)
                        .setTitle("Refuse?")
                        .setMessage("Do you want to refuse this Passenger?")
                        .setPositiveButton("Confirm", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button Confirm = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Query query = databaseReference.child("CarpoolList");
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Subscriber");

                       // query.addValueEventListener(new ValueEventListener() {
                           // @Override
                            //public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    //if (dataSnapshot.child("PassengerID").getValue().toString().equals(passID)) {
                                        //String IDVehicle = dataSnapshot.child("vehicleID").getValue().toString();
                                        databaseReference.child(passID).removeValue();
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
                        alertDialog.dismiss();
                        Intent intent = new Intent(getApplication(), OperatorHomepageActivity.class);
                        startActivity(intent);
                        Toast.makeText(ViewPassenger.this, "Refused Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}