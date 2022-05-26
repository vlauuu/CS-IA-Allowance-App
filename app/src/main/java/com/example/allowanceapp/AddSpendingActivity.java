package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AddSpendingActivity extends AppCompatActivity {

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

    public void addSpending(View v)
    {
        String itemString = itemField.getText().toString();
        String costString = costField.getText().toString();
        String descriptionString = descriptionField.getText().toString();

        Double costDouble = Double.parseDouble(costString);

        Spending newSpending = new Spending(mUser.getEmail(), itemString, date, costDouble, descriptionString, false);

        firestore.collection("Spendings").document(
                itemString + " " + date).set(newSpending);

        Toast.makeText(getApplicationContext(),"Spending Added", Toast.LENGTH_LONG).show();

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

                                    Double newAllowance = thisAllowance - costDouble;

                                    firestore.collection("Users").document(thisEmail).update("allowance", newAllowance);
                                }
                            }
                        }
                    }
                });
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