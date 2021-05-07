package com.example.schooltranspo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DriverEditProfileActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023;
    public static final String TAG = "TAG";
    EditText fName, email, contact, gender, address, license;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;
    Button save;
    FirebaseUser firebaseUser;
    ImageButton driverImage;
    StorageReference storageReference;
    UserData ud =new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_edit_profile);

        fName = findViewById(R.id.etDriverEditProfileFullName);
        email = findViewById(R.id.etDriverEditProfileEmail);
        contact = findViewById(R.id.etDriverEditProfileContact);
        gender = findViewById(R.id.etDriverEditProfileGender);
        address = findViewById(R.id.etDriverEditProfileAddress);
        license = findViewById(R.id.etDriverEditProfileLicense);

        ud = (UserData)getIntent().getSerializableExtra("UD");

        fName.setText(ud.getFullName());
        email.setText(ud.getEmail());
        contact.setText(ud.getContact());
        gender.setText(ud.getGender());
        address.setText(ud.getAddress());
        license.setText(ud.getLicense());

        driverImage = findViewById(R.id.ibDriverEditProfile);
        save = findViewById(R.id.btnDriverEditProfileSave);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = firebaseAuth.getCurrentUser().getUid();
        firebaseUser = firebaseAuth.getCurrentUser();


        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(driverImage);
            }
        });

        driverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || contact.getText().toString().isEmpty() || gender.getText().toString().isEmpty() || address.getText().toString().isEmpty() || license.getText().toString().isEmpty()){
                    Toast.makeText(DriverEditProfileActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emailSave = email.getText().toString();
                firebaseUser.updateEmail(emailSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Email",emailSave);
                        edited.put("FullName", fName.getText().toString());
                        edited.put("Contact", contact.getText().toString());
                        edited.put("Gender", gender.getText().toString());
                        edited.put("Address", address.getText().toString());
                        edited.put("SchoolAddress", license.getText().toString());
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ud.setFullName(fName.getText().toString());
                                ud.setEmail(email.getText().toString());
                                ud.setContact(contact.getText().toString());
                                ud.setGender(gender.getText().toString());
                                ud.setAddress(address.getText().toString());
                                ud.setSchoolAddress(license.getText().toString());
                                Toast.makeText(DriverEditProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), DriverHomepageActivity.class);
                                i.putExtra("UD",ud);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DriverEditProfileActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(DriverEditProfileActivity.this, "Email is changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverEditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //passengerImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(driverImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DriverEditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}