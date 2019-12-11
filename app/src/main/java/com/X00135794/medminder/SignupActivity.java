package com.X00135794.medminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
    FirebaseFirestore db;
    private static final String TAG = "SignupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*asdasd*/
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
        toolbar.setTitle("Sign up");

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick( View view){
                progressbar.setVisibility(View.VISIBLE);
                if(! hasValidationErrors(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),phone.getText().toString(), county.getText().toString())){
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                        password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    createUserEntry();
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

            }
            }
        });

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }
        });


    }
    private void createUserEntry (){
                final String fName = firstName.getText().toString().trim();
                final String lName = lastName.getText().toString().trim();
                final  String uEmail = email.getText().toString().trim();
                final String uPhone = phone.getText().toString().trim();
                final String uCounty = county.getText().toString().trim();

                /*Map<String, Object> user = new HashMap<>();
                user.put("firstName", fName);
                user.put("lastName", lName);
                user.put("email", uEmail);
                user.put("phone", uPhone);
                user.put("county", uCounty);*/

                User user = new User(fName,lName, uEmail,uPhone, uCounty);

                // Add a new document with a generated ID
                db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
    }

    private boolean hasValidationErrors(String fName, String lName, String uEmail, String phoneNo, String uCounty){
        if(fName.isEmpty()){
            firstName.setError("First name required");
            firstName.requestFocus();
            return true;
        }
        if(lName.isEmpty()){
            lastName.setError("First name required");
            lastName.requestFocus();
            return true;
        }
//                if(gen.isEmpty()){
//                    genBtn.setError("Gender required");
//                    genBtn.requestFocus();
//                    return true;
//                }
        if(uEmail.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return true;
        }
        if(Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()){
            email.setError("Enter valid Email");
            email.requestFocus();
            return true;
        }
        if(phoneNo.isEmpty()){
            phone.setError("Phone required");
            phone.requestFocus();
            return true;
        }
        if(uCounty.isEmpty()){
            county.setError("County required");
            county.requestFocus();
            return true;
        }

        return false;
    };
}