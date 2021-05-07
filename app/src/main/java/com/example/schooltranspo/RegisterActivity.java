package com.example.schooltranspo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText fullname, email, password, confPassword, contact, eContact, gender, address, sAddress;
    Button register, login;
    CheckBox passenger, operator;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    UserData ud =new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.etRegisterFullname);
        email = findViewById(R.id.etRegisterEmail);
        password = findViewById(R.id.etRegisterPassword);
        confPassword = findViewById(R.id.etRegisterConfPassword);
        contact = findViewById(R.id.etRegisterContact);
        eContact = findViewById(R.id.etRegisterEContact);
        gender = findViewById(R.id.etRegisterGender);
        address = findViewById(R.id.etRegisterAddress);
        sAddress = findViewById(R.id.etRegisterSAddress);
        register = findViewById(R.id.btnRegister);
        login = findViewById(R.id.btnRegisterLogin);
        passenger = findViewById(R.id.cbPassenger);
        operator = findViewById(R.id.cbOperator);

        //ud = (UserData)getIntent().getSerializableExtra("UD");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        passenger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    operator.setChecked(false);
                    eContact.setEnabled(true);
                    sAddress.setEnabled(true);
                }
            }
        });

        operator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    passenger.setChecked(false);
                    eContact.setEnabled(false);
                    eContact.setText("");
                    sAddress.setEnabled(false);
                    sAddress.setText("");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = password.getText().toString().trim();
                String conPass = confPassword.getText().toString().trim();

                if(!(passenger.isChecked() || operator.isChecked())){
                    Toast.makeText(RegisterActivity.this, "Select an account type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fullname.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confPassword.getText().toString().isEmpty() || contact.getText().toString().isEmpty() || gender.getText().toString().isEmpty() || address.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pass.length() < 6){
                    password.setError("Password must be more than 6 characters");
                    return;
                }

                if (!pass.equals(conPass)){
                    confPassword.setError("Password does not match");
                    return;
                }

                //GGEZ
                if(valid){
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullname.getText().toString());
                            userInfo.put("Email",email.getText().toString());
                            userInfo.put("Contact", contact.getText().toString());
                            userInfo.put("EmergencyContact", eContact.getText().toString());
                            userInfo.put("Gender", gender.getText().toString());
                            userInfo.put("Address", address.getText().toString());
                            userInfo.put("SchoolAddress", sAddress.getText().toString());

                            ud.setFullName(fullname.getText().toString());
                            ud.setEmail(email.getText().toString());
                            ud.setContact(contact.getText().toString());
                            ud.setEmergencyContact(eContact.getText().toString());
                            ud.setGender(gender.getText().toString());
                            ud.setAddress(address.getText().toString());
                            ud.setSchoolAddress(sAddress.getText().toString());

                            if(passenger.isChecked()){
                                userInfo.put("UserType", "Passenger");
                            }
                            if(operator.isChecked()){
                                userInfo.put("UserType", "Operator");
                            }
                            df.set(userInfo);
                            if(passenger.isChecked()){
                                Intent i = new Intent(getApplicationContext(), PassengerHomepageActivity.class);
                                i.putExtra("UD",ud);
                                startActivity(i);
                                finish();
                            }
                            if(operator.isChecked()){
                                Intent i = new Intent(getApplicationContext(), OperatorHomepageActivity.class);
                                i.putExtra("UD",ud);
                                startActivity(i);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }

}