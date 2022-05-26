package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);

    }

    public void SignIn(View v) {

        String usernameInput = emailField.getText().toString();
        String passwordInput = passwordField.getText().toString();

        mAuth.signInWithEmailAndPassword(usernameInput, passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                    Toast.makeText(getApplicationContext(), "Correct email and password, welcome!", Toast.LENGTH_SHORT).show();

                } else {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Incorrect email or password, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent startPage = new Intent(this, HomeActivity.class);
            startPage.putExtra("currUser", (Serializable) currentUser);
            startActivity(startPage);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Paramenters not filled", Toast.LENGTH_LONG).show();
        }
    }

    public void SignUp(View v) {

        String emailInput = emailField.getText().toString();
        String passwordInput = passwordField.getText().toString();

        if(!emailInput.isEmpty() && !passwordInput.isEmpty()){
            User currentUser = new User(emailInput, passwordInput, 0.0);
            fireStore.collection("Users").document(emailInput).set(currentUser);

            Toast.makeText(getApplicationContext(),"Signed up!", Toast.LENGTH_LONG).show();

            Intent startPage = new Intent(this, HomeActivity.class);
            startActivity(startPage);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Paramenters not filled", Toast.LENGTH_LONG).show();
        }
    }
}