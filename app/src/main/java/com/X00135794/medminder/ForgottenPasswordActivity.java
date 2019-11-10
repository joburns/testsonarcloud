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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText userEmail;
    Button forgotPass;
    Button back;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        toolbar = findViewById(R.id.toolbar3);
        progressBar= findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.etUserEmailForPassReset);
        forgotPass = findViewById(R.id.btnForgotPassword);
        back = findViewById(R.id.btnBack);
        toolbar.setTitle("Forgot Password");

        firebaseAuth = FirebaseAuth.getInstance();

        forgotPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgottenPasswordActivity.this, "Password reset email was sent to your email", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgottenPasswordActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(ForgottenPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }

                });
            }
        });

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgottenPasswordActivity.this, MainActivity.class));
            }
        });
    }
}
