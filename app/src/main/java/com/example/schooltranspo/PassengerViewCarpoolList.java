package com.example.schooltranspo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PassengerViewCarpoolList extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    ArrayList<Subscriber> subscriberList;
    RcyclerViewPassengerAdapter rcyclerViewPassengerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_view_carpool_list);

        Toolbar toolbar = findViewById(R.id.passengerToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Available Carpools");
            actionBar.show();
        }

        recyclerView = findViewById(R.id.recyclerViewPassenger);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();

        subscriberList = new ArrayList<>();

        clearAll();

        GetDataFromFirebase();
    }

    private void GetDataFromFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String passID = user.getUid();

        Query query = myRef.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clearAll();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Subscriber subscriber = new Subscriber();
                    if(!dataSnapshot.child("driverName").getValue().toString().equals("No Assign Driver")) {
//                    final String[] presence = new String[1];
//                    presence[0] = "";
//                    if (dataSnapshot.child("passengerID").getValue().toString().equals(passID)) {
//                        Query query1 = myRef.child("Subscriber");
//                        query1.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
//                                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
//                                    System.out.println(dataSnapshot.child("driverName").getValue().toString());
//                                if(dataSnapshot1.child("passengerID").getValue().toString().equals(passID)) {
//                                    presence[0] = "Found";
//                                    System.out.println(presence[0]);
//                                    System.out.println(dataSnapshot1.child("driverName").getValue().toString());
//                                }
//                                if(presence[0].equals("Found")) {
                        subscriber.setDriverName(dataSnapshot.child("driverName").getValue().toString());
                        subscriber.setVehicles(dataSnapshot.child("vehicles").getValue().toString());
                        subscriber.setVehicleID(dataSnapshot.child("vehicleID").getValue().toString());
                        subscriber.setOperatorID(dataSnapshot.child("operatorID").getValue().toString());
                        subscriber.setPassengerID(passID);
//                                    subscriber.setStatus(dataSnapshot1.child("status").getValue().toString());

                        subscriberList.add(subscriber);
//                                }
                    }
                }
                rcyclerViewPassengerAdapter = new RcyclerViewPassengerAdapter(PassengerViewCarpoolList.this, subscriberList);
                recyclerView.setAdapter(rcyclerViewPassengerAdapter);
                rcyclerViewPassengerAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                    }
            }

            //           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void clearAll(){
        if (subscriberList != null){
            subscriberList.clear();

            if(rcyclerViewPassengerAdapter != null){
                rcyclerViewPassengerAdapter.notifyDataSetChanged();
            }
        }

        subscriberList = new ArrayList<>();
    }
}