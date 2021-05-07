package com.example.schooltranspo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverOperatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DriverOperatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverOperatorFragment newInstance(String param1, String param2) {
        DriverOperatorFragment fragment = new DriverOperatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DriverOperatorFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    //CardView cardView;

    private DatabaseReference myRef;
    private ArrayList<Subscriber> subscriberList;
    private RecyclerViewOperator2Adapter recyclerViewOperator2Adapter;
    private FirebaseFirestore firebaseFirestore;

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
        View view = inflater.inflate(R.layout.fragment_driver_operator, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewRequest);
        //cardView = (CardView) view.findViewById(R.id.vehicleListCardView);

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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference docRef = firebaseFirestore.collection("Users");

        String OperatorID = user.getUid();

        Query query = myRef.child("Subscriber");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clearAll();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Subscriber subscriber = new Subscriber();

                    if(dataSnapshot.child("operatorID").getValue().toString().equals(OperatorID) && dataSnapshot.child("status").getValue().toString().equals("Waiting for Confirmation")) {
                        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                                    String passID = qdSnapshot.getId();
                                    if(dataSnapshot.child("passengerID").getValue().toString().equals(passID)) {
                                        subscriber.setPassengerName(qdSnapshot.getString("FullName"));
                                        subscriber.setVehicles(dataSnapshot.child("vehicles").getValue().toString());
                                        subscriber.setStatus(dataSnapshot.child("status").getValue().toString());
                                        subscriber.setVehicleID(dataSnapshot.child("vehicleID").getValue().toString());
                                        subscriber.setPassengerID(passID);

                                        subscriberList.add(subscriber);
                                    }
                                }
                                recyclerViewOperator2Adapter = new RecyclerViewOperator2Adapter(getContext(), subscriberList);
                                recyclerView.setAdapter(recyclerViewOperator2Adapter);
                                recyclerViewOperator2Adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                //recyclerViewOperatorAdapter = new RecyclerViewOperatorAdapter(getApplicationContext(), operatorList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void clearAll(){
        if (subscriberList != null){
            subscriberList.clear();

            if(recyclerViewOperator2Adapter != null){
                recyclerViewOperator2Adapter.notifyDataSetChanged();
            }
        }

        subscriberList = new ArrayList<>();
    }
}