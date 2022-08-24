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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

/**
 * It allows the user to add to their allowance with their entered mvalue
 *
 * @author Vico Lau
 * @version 0.1
 */

public class AddAllowanceActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore firestore;

    private EditText addAllowanceField;
    String myUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allowance);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        addAllowanceField = findViewById(R.id.editTextAddAllowance);

        Intent intent = getIntent();
        myUserEmail = intent.getExtras().getString("currUser");

    }

    public void addAllowance(View v)
    {
        String aAString = addAllowanceField.getText().toString();

        if(aAString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "There are empty field(s), please try again ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Double addAllowanceDouble = Double.parseDouble(aAString);

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

                                    if (thisEmail.equals(myUserEmail))
                                    {
                                        Double thisAllowance = doc.getDouble("allowance");

                                        Double newAllowance = thisAllowance + addAllowanceDouble;

                                        firestore.collection("Users").document(thisEmail).update("allowance", newAllowance);

                                        Toast.makeText(getApplicationContext(), "Allowance updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
        }

    }
}