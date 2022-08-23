package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The user is able to see the months' spendings with the cost and name of the item showed
 * It can be clicked on to see further information about the spendings
 *
 * @author Vico Lau
 * @version 0.1
 */

public class MonthlySpendingActivity extends AppCompatActivity {

    RecyclerView recView;
    private FirebaseFirestore firestore;
    ArrayList<String> nameInfo = new ArrayList<>();
    ArrayList<String> statusInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_spending);

        recView = findViewById(R.id.recView2);
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String myUserEmail = intent.getExtras().getString("currUser");
        String monthYear = intent.getExtras().getString("currMonth");

        MonthlySpendingAdapter myAdapter = new MonthlySpendingAdapter(nameInfo, statusInfo
                , this, myUserEmail);

        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));

        updateRecView();
    }

    /**
     * creates a rec view of all the spendings in the month
     */

    public void updateRecView()
    {
        firestore.collection("Spendings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            List<DocumentSnapshot> ds = task.getResult().getDocuments();

                            for(DocumentSnapshot doc : ds)
                            {
                                Map<String, Object> docData = doc.getData();

                                Intent intent = getIntent();
                                String monthYear = intent.getExtras().getString("currMonth");
                                String myUserEmail = intent.getExtras().getString("currUser");

                                List<String> currDateList = Arrays.asList(monthYear.split("/"));

                                String currMonth = currDateList.get(0);

                                String thisDate = (String) docData.get("date");

                                List<String> dateList = Arrays.asList(thisDate.split("/"));

                                System.out.println(dateList.get(0));

                                String thisMonth = dateList.get(0);

                                if(!(boolean)docData.get("deleted"))
                                {
                                    if(myUserEmail.equals((String) docData.get("email")))
                                    {
                                        if(thisMonth.equals(currMonth)){
                                            String currItem = (String) docData.get("item");
                                            nameInfo.add(currItem);

                                            int currCost = doc.getLong("cost").intValue();
                                            String currDate = (String) docData.get("date");

                                            statusInfo.add("Date: " + currDate + " Cost: "
                                                    + currCost);
                                        }
                                    }
                                }

                            }

                            MonthlySpendingAdapter a = (MonthlySpendingAdapter)
                                    recView.getAdapter();
                            a.changeInfo(nameInfo, statusInfo);
                            a.notifyDataSetChanged();
                        }
                    }
                });

    }
}