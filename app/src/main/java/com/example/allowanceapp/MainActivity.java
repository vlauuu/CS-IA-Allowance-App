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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);

    }

    public void SignIn(View v) {

        String emailInput = emailField.getText().toString();
        String passwordInput = passwordField.getText().toString();

        firestore.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            List<DocumentSnapshot> ds = task.getResult().getDocuments();

                            for(DocumentSnapshot doc : ds)
                            {
                                Map<String, Object> docData = doc.getData();

                                String thisEmail = (String) docData.get("email");
                                String thisPassword = (String) docData.get("password");

                                if (thisEmail.equals(emailInput))
                                {
                                    if (thisPassword.equals(passwordInput))
                                    {
                                        updateUI(thisEmail);
                                        Toast.makeText(getApplicationContext(), "Correct email and password, welcome!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });

//        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                    updateUI(currentUser);
//                    Toast.makeText(getApplicationContext(), "Correct email and password, welcome!", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Incorrect email or password, please try again.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void updateUI(String thisEmail) {
        Intent startPage = new Intent(this, HomeActivity.class);
        startPage.putExtra("currUser", thisEmail);
        startActivity(startPage);
    }

//    public void updateUI(User currentUser) {
//        Intent startPage = new Intent(this, HomeActivity.class);
//        startPage.putExtra("currUser", (Serializable) currentUser);
//        startActivity(startPage);
//    }

    public void SignUp(View v) {

        String emailInput = emailField.getText().toString();
        String passwordInput = passwordField.getText().toString();

        System.out.println(emailInput + passwordInput);

        User currentUser = new User(emailInput, passwordInput, 0.0);
        firestore.collection("Users").document(emailInput).set(currentUser);
        Toast.makeText(getApplicationContext(),"You are now signed up! Feel free to log in", Toast.LENGTH_LONG).show();
//        updateUI(currentUser);


//        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                    updateUI(currentUser);
//                    Toast.makeText(getApplicationContext(),"Signed up!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),"Paramenters not filled", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

}