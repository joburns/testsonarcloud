package com.X00135794.medminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressbar;
    EditText email;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText phone;
    EditText county;
    Button signup;
    Button back;
    FirebaseAuth firebaseAuth;
   // FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        toolbar = findViewById(R.id.toolbarSignup);
        progressbar = findViewById(R.id.progressBar);
        email = findViewById(R.id.etEmailSignup);
        password = findViewById(R.id.etPasswordSignup);
        firstName = findViewById(R.id.etFirstNameSignup);
        lastName = findViewById(R.id.etLastNameSignup);
        phone = findViewById(R.id.etPhoneSignup);
        county = findViewById(R.id.etCountySignup);
        signup = findViewById(R.id.btnSignUp);
        back = findViewById(R.id.btnBack);
        toolbar.setTitle(R.string.app_name);

        firebaseAuth = FirebaseAuth.getInstance();
   //     db = FirebaseFirestore.getInstance();
  //      private boolean hasValidationErrors(){
//
//            if(fName.isEmpty()){
//                firstName.setError("First name required");
//                firstName.requestFocus();
//                return true;
//            }
//            if(lName.isEmpty()){
//                lastName.setError("First name required");
//                lastName.requestFocus();
//                return true;
//            }
////                if(gen.isEmpty()){
////                    genBtn.setError("Gender required");
////                    genBtn.requestFocus();
////                    return true;
////                }
//            if(uEmail.isEmpty()){
//                email.setError("email required");
//                email.requestFocus();
//                return true;
//            }
//            if(phoneNo.isEmpty()){
//                phone.setError("Phone required");
//                phone.requestFocus();
//                return true;
//            }
//            if(uCounty.isEmpty()){
//                county.setError("County required");
//                county.requestFocus();
//                return true;
//            }

  //          return false;
 //       };

        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick( View view){
                progressbar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                        password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){


                                                Toast.makeText(SignupActivity.this, "Registered successfully Please check your email for verification", Toast.LENGTH_LONG).show();
                                                email.setText("");
                                                password.setText("");
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            }else{
                                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                /*.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String fName = firstName.getText().toString().trim();
                        final String lName = lastName.getText().toString().trim();
                        final  String uEmail = email.getText().toString().trim();
                        final String uPhone = phone.getText().toString().trim();
                        final String uCounty = county.getText().toString().trim();

                        CollectionReference dbUsers = db.collection("users");
                            User user = new User(
                                    fName,
                                    lName,
                                    uEmail,
                                    uPhone,
                                    uCounty
                            );
                        dbUsers.add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(SignupActivity.this, "User Added", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                        }
                });*/
            }
        });

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }
        });


    }
}