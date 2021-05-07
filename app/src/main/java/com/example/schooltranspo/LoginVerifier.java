package com.example.schooltranspo;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class LoginVerifier extends AppCompatActivity implements Serializable {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    UserData ud = new UserData();

    protected void LoginCheck(UserData ud, MainActivity ma) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference df = firebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Intent intent = null;
                    if (documentSnapshot.getString("UserType").equals("Passenger")) {
                        ud.setFullName(documentSnapshot.getString("FullName"));
                        ud.setEmail(documentSnapshot.getString("Email"));
                        ud.setContact(documentSnapshot.getString("Contact"));
                        ud.setEmergencyContact(documentSnapshot.getString("EmergencyContact"));
                        ud.setGender(documentSnapshot.getString("Gender"));
                        ud.setAddress(documentSnapshot.getString("Address"));
                        ud.setSchoolAddress(documentSnapshot.getString("SchoolAddress"));

                         intent = new Intent(ma.getApplicationContext(), PassengerHomepageActivity.class);
                    }
                    else if (documentSnapshot.getString("UserType").equals("Operator")) {
                        ud.setFullName(documentSnapshot.getString("FullName"));
                        ud.setEmail(documentSnapshot.getString("Email"));
                        ud.setContact(documentSnapshot.getString("Contact"));
                        ud.setGender(documentSnapshot.getString("Gender"));
                        ud.setAddress(documentSnapshot.getString("Address"));

                         intent = new Intent(ma.getApplicationContext(), OperatorHomepageActivity.class);
                    }
                    else if (documentSnapshot.getString("UserType").equals("Driver")) {
                        ud.setFullName(documentSnapshot.getString("FullName"));
                        ud.setEmail(documentSnapshot.getString("Email"));
                        ud.setContact(documentSnapshot.getString("Contact"));
                        ud.setGender(documentSnapshot.getString("Gender"));
                        ud.setAddress(documentSnapshot.getString("Address"));
                        ud.setLicense(documentSnapshot.getString("License"));

                         intent = new Intent(ma.getApplicationContext(), DriverHomepageActivity.class);
                    }
                    else if (documentSnapshot.getString("UserType").equals("Admin")) {
                            ud.setFullName(documentSnapshot.getString("FullName"));
                            intent = new Intent(ma.getApplicationContext(), AdminHompageActivity.class);
                    }
                    else{
                        Log.d("TAG", "onSuccess:" + documentSnapshot.get("UserType"));
                    }
                    intent.putExtra("UD", ud);
                    ma.startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
        }else{
            FirebaseAuth.getInstance().signOut();
            ma.startActivity(new Intent(ma.getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    public UserData GetUserData(){

        return ud;
    }
}
