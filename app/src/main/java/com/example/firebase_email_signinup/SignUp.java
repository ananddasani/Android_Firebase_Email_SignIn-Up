package com.example.firebase_email_signinup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText fullName, email, password, confPassword;
    Button signupButton;

    String userFullName, userEmail, userPassword, userConfirmPass;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.editTextTextFullName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPass);
        confPassword = findViewById(R.id.editTextTextConfirmPass);
        signupButton = findViewById(R.id.signUpButton);

        auth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userFullName = fullName.getText().toString();
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                userConfirmPass = confPassword.getText().toString();

                //chek the info given
                if (userFullName.equals("")) {
                    fullName.setError("Full Name Required");
                    return;
                }

                if (userEmail.equals("")) {
                    email.setError("Email Required");
                    return;
                }

                if (userPassword.equals("")) {
                    password.setError("Password Required");
                    return;
                }

                if (userConfirmPass.equals("")) {
                    confPassword.setError("Confirm Password Required");
                    return;
                }

                //all info is given check for confirm password is same or not
                if (!userPassword.equals(userConfirmPass)) {
                    confPassword.setError("Confirm Password Doesn't Match");
                    return;
                }

                //all ok, signup the user
                auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUp.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "User Registration Failed :: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
