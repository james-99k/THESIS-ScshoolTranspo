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

public class DriverViewProfileActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    ImageView driverProfPic;
    TextView fName, email, contact,  gender, address, license;
    Button editDriverProfile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    StorageReference storageReference;
    UserData ud =new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_profile);

        editDriverProfile = findViewById(R.id.btnDriverViewProfileEdit);

        driverProfPic = findViewById(R.id.ibDriverViewProfile);

        fName = findViewById(R.id.tvDriverViewProfileFullName);
        email = findViewById(R.id.tvDriverViewProfileEmail);
        contact = findViewById(R.id.tvDriverViewProfileContact);
        gender = findViewById(R.id.tvDriverViewProfileGender);
        address = findViewById(R.id.tvDriverViewProfileAddress);
        license = findViewById(R.id.tvDriverViewProfileLicense);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = firebaseAuth.getCurrentUser().getUid();

        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(driverProfPic);
            }
        });

        ud = (UserData)getIntent().getSerializableExtra("UD");

        if(!ud.getFullName().equals("")){
            fName.setText(ud.getFullName());
            email.setText(ud.getEmail());
            contact.setText(ud.getContact());
            gender.setText(ud.getGender());
            address.setText(ud.getAddress());
            license.setText(ud.getLicense());
        }else{
            Log.d("TAG", "onEvent: Document does not exist");
        }

        editDriverProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DriverEditProfileActivity.class);
                i.putExtra("UD",ud);
                startActivity(i);
            }
        });
    }
}