package com.example.schooltranspo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OperatorAddCarpoolActivity extends AppCompatActivity {

    EditText brand, model, color, plateNo, capacity, schoolName, schoolDestination, serviceFee;
    ImageButton carpoolImage;
    Button add;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    //Spinner spinner;
    Operator operator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_add_carpool);

        brand = findViewById(R.id.etVehicleBrand);
        model = findViewById(R.id.etVehicleModel);
        color = findViewById(R.id.etVehicleColor);
        plateNo = findViewById(R.id.etVehiclePlateNo);
        capacity = findViewById(R.id.etVehicleCapacity);
        schoolName = findViewById(R.id.etVehicleSchoolName);
        schoolDestination = findViewById(R.id.etVehicleSchoolDestination);
        serviceFee = findViewById(R.id.etVehicleServiceFee);
        //spinner = findViewById(R.id.statusSpinner);

        add = findViewById(R.id.btnAddCarpool);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        operator = new Operator();

//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerAdapter);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brand.getText().toString().isEmpty() || model.getText().toString().isEmpty() || color.getText().toString().isEmpty() || plateNo.getText().toString().isEmpty() || capacity.getText().toString().isEmpty() || schoolName.getText().toString().isEmpty() || schoolDestination.getText().toString().isEmpty() || serviceFee.getText().toString().isEmpty()){
                    Toast.makeText(OperatorAddCarpoolActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
            }
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(OperatorAddCarpoolActivity.this, "Carpool Added", Toast.LENGTH_SHORT).show();
                DocumentReference df = firebaseFirestore.collection("Vehicles").document();
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("Operator ID",user.getUid());
                userInfo.put("Brand",brand.getText().toString());
                userInfo.put("Model",model.getText().toString());
                userInfo.put("Color", color.getText().toString());
                userInfo.put("PlateNumber",plateNo.getText().toString());
                userInfo.put("Capacity",capacity.getText().toString());
                userInfo.put("SchoolName",schoolName.getText().toString());
                userInfo.put("SchoolDestination",schoolDestination.getText().toString());
                userInfo.put("ServiceFee",serviceFee.getText().toString());
                String status = "Not Operational";
                userInfo.put("Status", status);
                df.set(userInfo);
                String vehicleID = df.getId();
                String operatorID = user.getUid();
                saveCarpool(vehicleID, operatorID);
                finish();
            }
        });
    }

    public void saveCarpool(String vID, String oID) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("CarpoolList");

        String driver = "No Assign Driver";
        operator.setVehicles(brand.getText().toString());
        operator.setDriverName(driver);
        operator.setOperatorID(oID);
        operator.setVehicleID(vID);


        rootRef.child(vID).setValue(operator);
    }
}