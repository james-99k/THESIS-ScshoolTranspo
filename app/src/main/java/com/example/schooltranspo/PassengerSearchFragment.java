package com.example.schooltranspo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerSearchFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerSearchFragment newInstance(String param1, String param2) {
        PassengerSearchFragment fragment = new PassengerSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerView;

    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Subscriber> subscriberList;
    private RcyclerViewPassengerAdapter rcyclerViewPassengerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPassenger);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();

        subscriberList = new ArrayList<>();

        clearAll();

        GetDataFromFirebase();

        return view;
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
                                rcyclerViewPassengerAdapter = new RcyclerViewPassengerAdapter(getContext(), subscriberList);
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