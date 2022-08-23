package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TThe client can see their allowance on the top of the screen
 * It also has buttons that help the user navigate to other activities
 *
 * @author Vico Lau
 * @version 0.1
 */

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore firestore;
    String myUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        myUserEmail = intent.getExtras().getString("currUser");

        TextView allowanceView = (TextView)findViewById(R.id.textViewAllowance);

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

                                Intent intent = getIntent();
                                myUserEmail = intent.getExtras().getString("currUser");

                                if (thisEmail.equals(myUserEmail))
                                {
                                    Double thisAllowance = doc.getDouble("allowance");

                                    allowanceView.setText("Current balance is: " + thisAllowance);
                                }
                            }
                        }
                    }
                });

    }

    public void updateAllowance(View v) {

        TextView allowanceView = (TextView)findViewById(R.id.textViewAllowance);

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

                                Intent intent = getIntent();
                                myUserEmail = intent.getExtras().getString("currUser");

                                if (thisEmail.equals(myUserEmail))
                                {
                                    Double thisAllowance = doc.getDouble("allowance");

                                    allowanceView.setText("Current balance is: " + thisAllowance);
                                }
                            }
                        }
                    }
                });
    }

    public void goToAddAllowance(View v) {
        Intent intent = getIntent();
        String myUserEmail = intent.getExtras().getString("currUser");

        Intent newIntent = new Intent(this, AddAllowanceActivity.class);
        newIntent.putExtra("currUser", myUserEmail);
        startActivity(newIntent);
        finish();
    }

    public void goToAddSpending(View v) {
        Intent intent = getIntent();
        String myUserEmail = intent.getExtras().getString("currUser");

        Intent newIntent = new Intent(this, AddSpendingActivity.class);
        newIntent.putExtra("currUser", myUserEmail);
        startActivity(newIntent);
        finish();
    }

    public void goToCheckSpending(View v) {
        Intent intent = getIntent();
        String myUserEmail = intent.getExtras().getString("currUser");

        Intent newIntent = new Intent(this, MonthOverviewActivity.class);
        newIntent.putExtra("currUser", myUserEmail);
        startActivity(newIntent);
        finish();
    }

}