package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class ListOfStudents extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    ArrayList<UserData> passengerList;
    RecyclerViewListOfStudents recyclerViewListOfStudents;
    String driverName, operatorID;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_students);

        Toolbar toolbar = findViewById(R.id.driverViewListPass);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("List of Your Passengers");
            actionBar.show();
        }

        Intent intent = getIntent();
        driverName = intent.getStringExtra("FullName");
        operatorID = intent.getStringExtra("OperatorID");

        recyclerView = findViewById(R.id.recyclerViewListPassenger);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        passengerList = new ArrayList<>();

        clearAll();

        GetDataFromFirebase();
    }

    private void GetDataFromFirebase() {
        Query query = myRef.child("Subscriber");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserData userData = new UserData();
                    if(dataSnapshot.child("driverName").getValue().toString().equals(driverName) && dataSnapshot.child("operatorID").getValue().toString().equals(operatorID)) {
                        String passID = dataSnapshot.child("passengerID").getValue().toString();

                        CollectionReference docRef = firebaseFirestore.collection("Users");

                        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                    String passengerID = qdSnapshot.getId();
                                    if(passengerID.equals(passID)) {
                                        userData.setFullName(qdSnapshot.getString("FullName"));
                                        userData.setAddress(qdSnapshot.getString("Address"));
                                        userData.setContact(qdSnapshot.getString("Contact"));
                                        userData.setSchoolName("School Name");// School Name
                                        userData.setPassengerID(passengerID);

                                        passengerList.add(userData);
                                    }
                                }
                                recyclerViewListOfStudents = new RecyclerViewListOfStudents(getApplicationContext(), passengerList);
                                recyclerView.setAdapter(recyclerViewListOfStudents);
                                recyclerViewListOfStudents.notifyDataSetChanged();
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
    private void clearAll(){
        if (passengerList != null){
            passengerList.clear();

            if(recyclerViewListOfStudents != null){
                recyclerViewListOfStudents.notifyDataSetChanged();
            }
        }

        passengerList = new ArrayList<>();
    }
}