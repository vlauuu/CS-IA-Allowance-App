package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * It allows the user to add their own spending to the data base after they fill in the
 * appropriate params
 *
 * @author Vico Lau
 * @version 0.1
 */

public class AddSpendingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore firestore;

    private String date;

    private EditText itemField;
    private EditText costField;
    private EditText descriptionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        itemField = findViewById(R.id.editTextItem);
        costField = findViewById(R.id.editTextCost);
        descriptionField = findViewById(R.id.editTextDescription);

        findViewById(R.id.showCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    /**
     * This method takes in a View
     * it adds a spending to the database with the added params
     *
     * @param v
     */

    public void addSpending(View v)
    {
        String itemString = itemField.getText().toString();
        String costString = costField.getText().toString();
        String descriptionString = descriptionField.getText().toString();


        if(itemString.isEmpty() || costString.isEmpty() || descriptionString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "There are empty field(s), please try again ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Double costDouble = Double.parseDouble(costString);

            Intent intent = getIntent();
            String myUserEmail = intent.getExtras().getString("currUser");

            Spending newSpending = new Spending(myUserEmail, itemString, date, costDouble, descriptionString, false);

            System.out.println(date);

            firestore.collection("Spendings").document(
                    itemString + " " + myUserEmail).set(newSpending);

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

                                        Double newAllowance = thisAllowance - costDouble;

                                        firestore.collection("Users").document(thisEmail).update("allowance", newAllowance);

                                        if(newAllowance > 0)
                                        {
                                            Toast.makeText(getApplicationContext(),"Spending Added", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"STOP SPENDING, YOU HAVE NO MORE MONEY", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }
                    });
        }

    }


    private void showDatePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        date = month + "/" + dayOfMonth + "/" + year;
    }
}