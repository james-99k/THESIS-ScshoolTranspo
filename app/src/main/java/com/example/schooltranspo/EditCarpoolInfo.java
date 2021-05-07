package com.example.schooltranspo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditCarpoolInfo extends AppCompatActivity {

    EditText brand, model, color, plateNo, capacity, schoolName, schoolDestination, serviceFee;
    Button save;
    Spinner spinner;
    String vehicleID;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Operator operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_carpool_info);

        Intent intent = getIntent();
        vehicleID = intent.getStringExtra("vehicleID");

        brand = findViewById(R.id.edVehicleBrand);
        model = findViewById(R.id.edVehicleModel);
        color = findViewById(R.id.edVehicleColor);
        plateNo = findViewById(R.id.edVehiclePlateNo);
        capacity = findViewById(R.id.edVehicleCapacity);
        schoolName = findViewById(R.id.edVehicleSchoolName);
        schoolDestination = findViewById(R.id.edVehicleSchoolDestination);
        serviceFee = findViewById(R.id.edVehicleServiceFee);
        spinner = findViewById(R.id.statusSpinner);

        save = findViewById(R.id.btnSaveCarpool);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        operator = new Operator();

        CollectionReference cf = firebaseFirestore.collection("Vehicles");

        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qdSnapshot : queryDocumentSnapshots) {
                    if(qdSnapshot.getId().equals(vehicleID)) {
                        brand.setText(qdSnapshot.getString("Brand"));
                        model.setText(qdSnapshot.getString("Model"));
                        color.setText(qdSnapshot.getString("Color"));
                        plateNo.setText(qdSnapshot.getString("PlateNumber"));
                        capacity.setText(qdSnapshot.getString("Capacity"));
                        schoolName.setText(qdSnapshot.getString("SchoolName"));
                        schoolDestination.setText(qdSnapshot.getString("SchoolDestination"));
                        serviceFee.setText(qdSnapshot.getString("ServiceFee"));
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brand.getText().toString().isEmpty() || model.getText().toString().isEmpty() || color.getText().toString().isEmpty() || plateNo.getText().toString().isEmpty() || capacity.getText().toString().isEmpty() || schoolName.getText().toString().isEmpty() || schoolDestination.getText().toString().isEmpty() || serviceFee.getText().toString().isEmpty()) {
                    Toast.makeText(EditCarpoolInfo.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(EditCarpoolInfo.this, "Carpool Added", Toast.LENGTH_SHORT).show();

                DocumentReference df = firebaseFirestore.collection("Vehicles").document(vehicleID);
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("Operator ID", user.getUid());
                userInfo.put("Brand",brand.getText().toString());
                userInfo.put("Model",model.getText().toString());
                userInfo.put("Color", color.getText().toString());
                userInfo.put("PlateNumber",plateNo.getText().toString());
                userInfo.put("Capacity",capacity.getText().toString());
                userInfo.put("SchoolName",schoolName.getText().toString());
                userInfo.put("SchoolDestination",schoolDestination.getText().toString());
                userInfo.put("ServiceFee",serviceFee.getText().toString());
                userInfo.put("Status", spinner.getSelectedItem().toString());
                df.update(userInfo);
                finish();
            }
        });
    }

}