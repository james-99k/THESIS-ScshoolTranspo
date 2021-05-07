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

public class OperatorViewProfileActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    ImageView operatorProfPic;
    TextView fName, email, contact, gender, address;
    Button editOperatorProfile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    StorageReference storageReference;
    UserData ud =new UserData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_view_profile);

        operatorProfPic = findViewById(R.id.ibOperatorViewProfile);

        fName = findViewById(R.id.tvOperatorViewProfileFullName);
        email = findViewById(R.id.tvOperatorViewProfileEmail);
        contact = findViewById(R.id.tvOperatorViewProfileContact);
        gender = findViewById(R.id.tvOperatorViewProfileGender);
        address = findViewById(R.id.tvOperatorViewProfileAddress);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = firebaseAuth.getCurrentUser().getUid();

        editOperatorProfile = findViewById(R.id.btnOperatorViewProfileEdit);

        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(operatorProfPic);
            }
        });

        ud = (UserData)getIntent().getSerializableExtra("UD");

        if(!ud.getFullName().equals("")){
            fName.setText(ud.getFullName());
            email.setText(ud.getEmail());
            contact.setText(ud.getContact());
            gender.setText(ud.getGender());
            address.setText(ud.getAddress());
        }else{
            Log.d("TAG", "onEvent: Document does not exist");
        }

        editOperatorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OperatorEditProfileActivity.class);
                i.putExtra("UD",ud);
                startActivity(i);
            }
        });
    }
}