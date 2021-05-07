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

public class OperatorAddDriverActivity extends AppCompatActivity {

    EditText fullName, email, password, confPassword;
    Button register;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_add_driver);

        fullName = findViewById(R.id.etRegisterFullname);
        email = findViewById(R.id.etRegisterEmail);
        password = findViewById(R.id.etRegisterPassword);
        confPassword = findViewById(R.id.etRegisterConfPassword);
        register = findViewById(R.id.btnRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String OperatorID = user.getUid();

        System.out.println(OperatorID);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(confPassword);

                String eMail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String conPass = confPassword.getText().toString().trim();

                if(TextUtils.isEmpty(eMail)){
                    email.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    password.setError("Password is required");
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

                if(valid){
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(OperatorAddDriverActivity.this, "Driver Added", Toast.LENGTH_SHORT).show();
                            DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("Operator ID", OperatorID);
                            userInfo.put("Email",email.getText().toString());
                            userInfo.put("UserType", "Driver");
                            df.set(userInfo);
                                finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OperatorAddDriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
}