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

import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
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

                                if (thisEmail.equals(mUser.getEmail()))
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
        if (mUser != null){
            Intent intent = new Intent(this, AddAllowanceActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToAddSpending(View v) {
        if (mUser != null){
            Intent intent = new Intent(this, AddSpendingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToCheckSpending(View v) {
        if (mUser != null){
            Intent intent = new Intent(this, MonthOverviewActivity.class);
            startActivity(intent);
            finish();
        }
    }
}