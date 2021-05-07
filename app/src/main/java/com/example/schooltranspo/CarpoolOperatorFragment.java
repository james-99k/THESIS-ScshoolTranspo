package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Use the {@link CarpoolOperatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarpoolOperatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarpoolOperatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarpoolOperatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarpoolOperatorFragment newInstance(String param1, String param2) {
        CarpoolOperatorFragment fragment = new CarpoolOperatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerView;
    //CardView cardView;

    private DatabaseReference myRef;
    private ArrayList<Operator> operatorList;
    private RecyclerViewOperatorAdapter recyclerViewOperatorAdapter;

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
        View view = inflater.inflate(R.layout.fragment_carpool_operator, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewOperator);
        //cardView = (CardView) view.findViewById(R.id.vehicleListCardView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();

        operatorList = new ArrayList<>();

        clearAll();

        GetDataFromFirebase();

//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //openCarpoolProfile();
//                Intent intent = new Intent(getActivity(), vehicleProfile.class);
//                getActivity().startActivity(intent);
//            }
//        });
        return view;
    }
    private void GetDataFromFirebase() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String OperatorID = user.getUid();

        Query query = myRef.child("CarpoolList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clearAll();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Operator operator = new Operator();

                    if(dataSnapshot.child("operatorID").getValue().toString().equals(OperatorID)) {

                        operator.setDriverName(dataSnapshot.child("driverName").getValue().toString());
                        operator.setVehicles(dataSnapshot.child("vehicles").getValue().toString());
                        operator.setVehicleID(dataSnapshot.child("vehicleID").getValue().toString());

                        operatorList.add(operator);
                    }
                }

                //recyclerViewOperatorAdapter = new RecyclerViewOperatorAdapter(getApplicationContext(), operatorList);
                recyclerViewOperatorAdapter = new RecyclerViewOperatorAdapter(getContext(), operatorList);
                recyclerView.setAdapter(recyclerViewOperatorAdapter);
                recyclerViewOperatorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void clearAll(){
        if (operatorList != null){
            operatorList.clear();

            if(recyclerViewOperatorAdapter != null){
                recyclerViewOperatorAdapter.notifyDataSetChanged();
            }
        }

        operatorList = new ArrayList<>();
    }
//    public void openCarpoolProfile(){
//        Intent intent = new Intent(getContext(), vehicleProfile.class);
//        startActivity(intent);
//    }
}