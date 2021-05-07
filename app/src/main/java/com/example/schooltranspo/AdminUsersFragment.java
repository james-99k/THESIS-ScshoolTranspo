package com.example.schooltranspo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminUsersFragment newInstance(String param1, String param2) {
        AdminUsersFragment fragment = new AdminUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerView;
    //CardView cardView;

    private FirebaseFirestore firebaseFirestore;;
    private ArrayList<UserData> userList;
    private RecyclerViewAdminUserAdapter recyclerViewAdminUserAdapter;

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
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAdmin);
        //cardView = (CardView) view.findViewById(R.id.vehicleListCardView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        firebaseFirestore = FirebaseFirestore.getInstance();

        userList = new ArrayList<>();

        clearAll();

        GetDataFromFirebase();

        return view;
    }
    private void GetDataFromFirebase() {
        CollectionReference docRef = firebaseFirestore.collection("Users");

        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                clearAll();
                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                    UserData userData = new UserData();
                    if(!qdSnapshot.getString("UserType").equals("Admin")) {
                        userData.setFullName(qdSnapshot.getString("FullName"));
                        userData.setContact(qdSnapshot.getString("Contact"));
                        userData.setAddress(qdSnapshot.getString("Address"));
                        userData.setUserType(qdSnapshot.getString("UserType"));

                        userList.add(userData);
                    }
                }
                recyclerViewAdminUserAdapter = new RecyclerViewAdminUserAdapter(getContext(), userList);
                recyclerView.setAdapter(recyclerViewAdminUserAdapter);
                recyclerViewAdminUserAdapter.notifyDataSetChanged();
            }
        });
    }
    private void clearAll(){
        if (userList != null){
            userList.clear();

            if(recyclerViewAdminUserAdapter != null){
                recyclerViewAdminUserAdapter.notifyDataSetChanged();
            }
        }

        userList = new ArrayList<>();
    }
}