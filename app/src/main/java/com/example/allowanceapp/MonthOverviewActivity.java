package com.example.allowanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonthOverviewActivity extends AppCompatActivity {

    RecyclerView recView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseFirestore firestore;
    String myUserEmail;
    ArrayList<String> nameInfo = new ArrayList<>();
    ArrayList<String> statusInfo = new ArrayList<>();

    Map<Integer, Integer> monthAndItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_overview);
        recView = findViewById(R.id.recView);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        myUserEmail = intent.getExtras().getString("currUser");

        SpendingsAdapter myAdapter = new SpendingsAdapter(nameInfo, statusInfo
                , this, myUserEmail);

        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));

        updateRecView();

    }

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

                                Boolean deletedOrNot = (Boolean) docData.get("deleted");

                                if(!deletedOrNot)
                                {
                                    String thisEmail = (String) docData.get("email");

                                    if(thisEmail.equals(myUserEmail))
                                    {
                                        String thisDate = (String) docData.get("date");

                                        List<String> dateList = Arrays.asList(thisDate.split("/"));

                                        System.out.println(dateList.get(0));

                                        String thisMonth = dateList.get(0);
                                        int thisMonthInt = Integer.parseInt(thisMonth);
                                        boolean noMatch = false;

//                                        if(!monthAndItems.isEmpty()){
//                                            for ( int key : monthAndItems.keySet()) {
//                                                if(key == thisMonthInt)
//                                                {
//                                                    monthAndItems.put(thisMonthInt, monthAndItems.get(key) + 1);
//                                                }
//                                                else
//                                                {
//                                                    noMatch = true;
//                                                }
//
//                                                if(noMatch){
//                                                    monthAndItems.put(thisMonthInt, 1);
//                                                }
//                                            }
//                                        }
//                                        else {
//                                            monthAndItems.put(thisMonthInt, 1);
//                                        }

                                        ArrayList<Integer> allKeys = new ArrayList<Integer>();

                                        for (Map.Entry<Integer,Integer> entry : monthAndItems.entrySet())
                                        {
                                            int thisKey = entry.getKey();
                                            allKeys.add(thisKey);
                                        }

                                        System.out.println(allKeys);

                                        if(allKeys.isEmpty())
                                        {
                                            monthAndItems.put(thisMonthInt, 1);
                                        }

                                        for ( int key : allKeys) {

                                            if(key == thisMonthInt)
                                            {
                                                int newValue = monthAndItems.get(key) + 1;
                                                monthAndItems.replace(thisMonthInt, newValue);
                                                System.out.println("Newly added " + monthAndItems.get(key));
                                                System.out.println(monthAndItems);
                                            }
                                            else
                                            {
                                                monthAndItems.put(thisMonthInt, 1);
                                            }
                                        }

                                    }
                                }
                            }

                            ArrayList<Integer> finalKeys = new ArrayList<Integer>();

                            for (Map.Entry<Integer,Integer> entry : monthAndItems.entrySet())
                            {
                                int thisKey = entry.getKey();
                                finalKeys.add(thisKey);
                            }

                            System.out.println("final keys: " + finalKeys);
                            System.out.println("final map: " + monthAndItems);


                            for( int key : finalKeys){

                                nameInfo.add(key + "/2022");
                                statusInfo.add("Items: " + monthAndItems.get(key));

                            }

                            System.out.println(nameInfo);
                            System.out.println(statusInfo);

                            SpendingsAdapter a = (SpendingsAdapter)
                                    recView.getAdapter();
                            a.changeInfo(nameInfo, statusInfo);
                            a.notifyDataSetChanged();
                        }
                    }
                });

    }


}