package com.example.schooltranspo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PassengerViewProfileActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    ImageView passengerProfPic;
    TextView fName, email, contact, eContact, gender, address, sAddress;
    Button editPassengerProfile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    StorageReference storageReference;
    UserData ud =new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_view_profile);

        passengerProfPic = findViewById(R.id.ibPassengerViewProfile);

        fName = findViewById(R.id.tvPassengerViewProfileFullName);
        email = findViewById(R.id.tvPassengerViewProfileEmail);
        contact = findViewById(R.id.tvPassengerViewProfileContact);
        eContact = findViewById(R.id.tvPassengerViewProfileEmergencyContact);
        gender = findViewById(R.id.tvPassengerViewProfileGender);
        address = findViewById(R.id.tvPassengerViewProfileAddress);
        sAddress = findViewById(R.id.tvPassengerViewProfileSchoolAddress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = firebaseAuth.getCurrentUser().getUid();

        editPassengerProfile = findViewById(R.id.btnPassengerViewProfileEdit);

        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(passengerProfPic);
            }
        });

        ud = (UserData)getIntent().getSerializableExtra("UD");

                if(!ud.getFullName().equals("")){
                    fName.setText(ud.getFullName());
                    email.setText(ud.getEmail());
                    contact.setText(ud.getContact());
                    eContact.setText(ud.getEmergencyContact());
                    gender.setText(ud.getGender());
                    address.setText(ud.getAddress());
                    sAddress.setText(ud.getSchoolAddress());
                }else{
                    Log.d("TAG", "onEvent: Document does not exist");
                }

        editPassengerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PassengerEditProfileActivity.class);
                i.putExtra("UD",ud);
                startActivity(i);
            }
        });
    }
}