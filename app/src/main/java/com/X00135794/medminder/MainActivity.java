package com.X00135794.medminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressbar;
    EditText email;
    EditText password;
    Button signup;
    Button login;
    Button forgotPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarLogin);
        progressbar = findViewById(R.id.progressBar);
        email = findViewById(R.id.etEmailLogin);
        password = findViewById(R.id.etPasswordLogin);
        signup = findViewById(R.id.btnRegister);
        login = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.btnUserForgotPass);
        toolbar.setTitle(R.string.app_name);

        firebaseAuth = FirebaseAuth.getInstance();



        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                progressbar.setVisibility(View.VISIBLE);
                if(! hasValidationErrors(email.getText().toString(),password.getText().toString())) {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString())
                            .addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressbar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            startActivity(new Intent(MainActivity.this, UserAccountActivity.class));
                                        } else {
                                            Toast.makeText(MainActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }));
                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgottenPasswordActivity.class));
            }
        });

    }

    private boolean hasValidationErrors(String uEmail, String uPassword) {
        if (uEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return true;
        }
        if(Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()){
            email.setError("Enter valid Email");
            email.requestFocus();
            return true;
        }
        if (uPassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return true;
        }
        return false;
    }
}
