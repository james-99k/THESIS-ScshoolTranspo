package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerHomeFragment extends Fragment implements OnMapReadyCallback {

  //  GoogleMap mapAPI;
 //   SupportMapFragment mapFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerHomeFragment newInstance(String param1, String param2) {
        PassengerHomeFragment fragment = new PassengerHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    CardView cardView;
    TextView vehicleName, driverName, notSubscribe;
    private DatabaseReference myRef;

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
        View view = inflater.inflate(R.layout.fragment_passenger_home, container, false);

        cardView = view.findViewById(R.id.subscribedLayout);
        vehicleName = view.findViewById(R.id.VehicleName);
        driverName = view.findViewById(R.id.driverName);
        notSubscribe = view.findViewById(R.id.notSubscribed);

        myRef = FirebaseDatabase.getInstance().getReference();

        searchData();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vehicleName.getText().toString().equals("")) {
                    String vName = vehicleName.getText().toString();
                    String driver = driverName.getText().toString();
                    Intent intent = new Intent(getActivity(), ViewSubscribedVehilceData.class);
                    intent.putExtra("vehicleBrand", vName);
                    intent.putExtra("driverName", driver);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getActivity(), "You have not yet subscribe", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

     //   mapAPI = googleMap;

     //   LatLng Mandaue = new LatLng(10.331621, 123.932136);

     //   mapAPI.addMarker(new MarkerOptions().position(Mandaue).title("Mandaue"));
     //   mapAPI.moveCamera(CameraUpdateFactory.newLatLng(Mandaue));
    }
    public void searchData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String passID = user.getUid();

        Query query = myRef.child("Subscriber");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("passengerID").getValue().toString().equals(passID) && dataSnapshot.child("status").getValue().toString().equals("Subscribed")) {
                        vehicleName.setText(dataSnapshot.child("vehicles").getValue().toString());
                        driverName.setText(dataSnapshot.child("driverName").getValue().toString());
                        notSubscribe.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}