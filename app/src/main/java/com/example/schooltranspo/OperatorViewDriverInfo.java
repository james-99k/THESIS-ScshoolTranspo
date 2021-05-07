package com.example.schooltranspo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OperatorViewDriverInfo extends AppCompatActivity {

    TextView fName, email, contact,  gender, address, license;
    String driverName;
    FirebaseFirestore firebaseFirestore;
    String driverID;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_view_driver_info);

        Toolbar toolbar = findViewById(R.id.operatorViewDriverInfoToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("View Driver Info");
            actionBar.show();
        }

        Intent intent = getIntent();
        driverName = intent.getStringExtra("driverName");

        fName = findViewById(R.id.inDriverViewProfileFullName);
        email = findViewById(R.id.inDriverViewProfileEmail);
        contact = findViewById(R.id.inDriverViewProfileContact);
        gender = findViewById(R.id.inDriverViewProfileGender);
        address = findViewById(R.id.inDriverViewProfileAddress);
        license = findViewById(R.id.inDriverViewProfileLicense);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        CollectionReference docRef = firebaseFirestore.collection("Users");

        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                    if(qdSnapshot.getString("FullName").equals(driverName)) {
                        fName.setText(qdSnapshot.getString("FullName"));
                        email.setText(qdSnapshot.getString("Email"));
                        contact.setText(qdSnapshot.getString("Contact"));
                        gender.setText(qdSnapshot.getString("Gender"));
                        address.setText(qdSnapshot.getString("Address"));
                        license.setText(qdSnapshot.getString("License"));
                    }
                }
            }
        });
    }
}