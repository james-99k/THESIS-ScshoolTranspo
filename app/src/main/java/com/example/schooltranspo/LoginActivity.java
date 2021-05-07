package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login, register;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    UserData ud = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etLoginEmail);
        password = findViewById(R.id.etLoginPassword);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnLoginRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(email);
                checkField(password);

                if(valid){
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess:" +documentSnapshot.getString("UserType"));

                if(documentSnapshot.getString("UserType").equals("Passenger")){
                    ud.setFullName(documentSnapshot.getString("FullName"));
                    ud.setEmail(documentSnapshot.getString("Email"));
                    ud.setContact(documentSnapshot.getString("Contact"));
                    ud.setEmergencyContact(documentSnapshot.getString("EmergencyContact"));
                    ud.setGender(documentSnapshot.getString("Gender"));
                    ud.setAddress(documentSnapshot.getString("Address"));
                    ud.setSchoolAddress(documentSnapshot.getString("SchoolAddress"));
                    Intent i = new Intent(getApplicationContext(), PassengerHomepageActivity.class);
                    i.putExtra("UD",ud);
                    startActivity(i);
                }
                else if(documentSnapshot.getString("UserType").equals("Operator")){
                    ud.setFullName(documentSnapshot.getString("FullName"));
                    ud.setEmail(documentSnapshot.getString("Email"));
                    ud.setContact(documentSnapshot.getString("Contact"));
                    ud.setGender(documentSnapshot.getString("Gender"));
                    ud.setAddress(documentSnapshot.getString("Address"));
                    Intent i = new Intent(getApplicationContext(), OperatorHomepageActivity.class);
                    i.putExtra("UD",ud);
                    startActivity(i);
                }
                else if(documentSnapshot.getString("UserType").equals("Driver")){
                    ud.setFullName(documentSnapshot.getString("FullName"));
                    ud.setEmail(documentSnapshot.getString("Email"));
                    ud.setContact(documentSnapshot.getString("Contact"));
                    ud.setGender(documentSnapshot.getString("Gender"));
                    ud.setAddress(documentSnapshot.getString("Address"));
                    ud.setLicense(documentSnapshot.getString("License"));
                    Intent i = new Intent(getApplicationContext(), DriverHomepageActivity.class);
                    i.putExtra("UD",ud);
                    startActivity(i);
                }
                else if(documentSnapshot.getString("UserType").equals("Admin")) {
                    ud.setFullName(documentSnapshot.getString("FullName"));
                    Intent i = new Intent(getApplicationContext(), AdminHompageActivity.class);
                    i.putExtra("UD", ud);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Invalid UserType", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        LoginVerifier lv = new LoginVerifier();
        lv.LoginCheck();
    }*/
}