package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class GiveFeedback extends AppCompatActivity {

    TextView driver;
    EditText comment;
    Button submit;
    String dName;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    Feedback feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        Toolbar toolbar = findViewById(R.id.feedbackToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Give Feedback");
            actionBar.show();
        }
        Intent intent = getIntent();
//        vName = intent.getStringExtra("VehicleBrand");
        dName = intent.getStringExtra("DriverName");

        driver = findViewById(R.id.driverName);
        comment = findViewById(R.id.commentBox);
        submit = findViewById(R.id.feedbackBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        feedback = new Feedback();

        driver.setText(dName);

        String userID = firebaseAuth.getUid();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReference.child("Subscriber");

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String tempPassID = dataSnapshot.child("passengerID").getValue().toString();
                            if(tempPassID.equals(userID)) {
                                String operatorID = dataSnapshot.child("operatorID").getValue().toString();
                                String driverName = dataSnapshot.child("driverName").getValue().toString();
                                String VehicleID = dataSnapshot.child("vehicleID").getValue().toString();

                                CollectionReference docRef = firebaseFirestore.collection("Users");

                                System.out.println(operatorID);
                                System.out.println(driverName);
                                System.out.println(VehicleID);

                                docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                            System.out.println(qdSnapshot.getString("FullName"));
                                            if(qdSnapshot.getString("UserType").equals("Driver") && qdSnapshot.getString("FullName").equals(driverName)) {
//                                            if(qdSnapshot.getString("FullName").equals(driverName)) {
                                                String driverID = qdSnapshot.getId();
                                                String cmt = comment.getText().toString();
                                                addDataToFirebase(userID, operatorID, VehicleID, driverID, cmt);
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
        });
    }

    public void addDataToFirebase(String uID, String oID, String vID, String dID, String comment) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("Feedback");

        feedback.setPassengerID(uID);
        feedback.setVehivleID(vID);
        feedback.setOperatorID(oID);
        feedback.setDriverID(dID);
        feedback.setComment(comment);


        rootRef.child(uID).setValue(feedback);
    }
}