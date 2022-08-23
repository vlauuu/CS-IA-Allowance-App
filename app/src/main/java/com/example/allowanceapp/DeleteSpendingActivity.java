package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
 * This deletes a spending when the user wishes to
 *
 * @author Vico Lau
 * @version 0.1
 */

public class DeleteSpendingActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_spending);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String myUserEmail = intent.getExtras().getString("currUser");
        String currItem = intent.getExtras().getString("currItem");

        TextView itemView = (TextView)findViewById(R.id.textViewItem);
        TextView dateView = (TextView)findViewById(R.id.textViewDate);
        TextView costView = (TextView)findViewById(R.id.textViewCost);
        TextView decriptionView = (TextView)findViewById(R.id.textViewDescription);

        firestore.collection("Spendings").get()
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

                                String thisItem = (String) docData.get("item");

                                if (thisItem.equals(currItem))
                                {
                                    String thisDate = (String) docData.get("date");
                                    int thisCost = doc.getLong("cost").intValue();
                                    String thisDescription = (String) docData.get("description");

                                    String thisCostS = String.valueOf(thisCost);

                                    itemView.setText(thisItem);
                                    dateView.setText(thisDate);
                                    costView.setText(thisCostS);
                                    decriptionView.setText(thisDescription);
                                }
                            }
                        }
                    }
                });

    }

    /**
     * This method takes in a View
     * It checks whether the email has this spending
     * Then it changes deleted status to true for the spending
     * It also adds back the cost of the item to the user's allowance
     *
     * @param v
     */

    public void deleteSpending(View v)
    {
        Intent intent = getIntent();
        String myUserEmail = intent.getExtras().getString("currUser");
        String currItem = intent.getExtras().getString("currItem");

        System.out.println(currItem);

        firestore.collection("Spendings").get()
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

                                String thisItem = (String) docData.get("item");

                                if (thisItem.equals(currItem))
                                {
                                    firestore.collection("Spendings")
                                            .document(currItem + " " +myUserEmail)
                                            .update("deleted", true);

                                    System.out.println("item changed");

                                    firestore.collection("Users").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        List<DocumentSnapshot> ds2 = task.getResult().getDocuments();

                                                        for(DocumentSnapshot doc2 : ds2)
                                                        {
                                                            Map<String, Object> docData2 = doc2.getData();

                                                            String thisEmail = (String) docData2.get("email");

                                                            if (thisEmail.equals(myUserEmail))
                                                            {
                                                                int ogBalance = doc2.getLong("allowance").intValue();
                                                                int itemCost = doc.getLong("cost").intValue();

                                                                int newAllowance = ogBalance + itemCost;

                                                                firestore.collection("Users")
                                                                        .document(myUserEmail)
                                                                        .update("allowance", newAllowance);

                                                                Toast.makeText(getApplicationContext(), "Spending deleted and you allowance has been updated", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });

    }
}