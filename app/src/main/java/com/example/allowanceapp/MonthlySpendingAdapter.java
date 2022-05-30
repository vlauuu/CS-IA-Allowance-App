package com.example.allowanceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MonthlySpendingAdapter extends RecyclerView.Adapter<SpendingViewHolder>{

    ArrayList<String> nameData;
    ArrayList<String> statusData;
    Context screen;
    String emailString;

    public MonthlySpendingAdapter(ArrayList<String> itemNames,
                            ArrayList<String> statusOutput,
                            Context screen,
                            String myUserEmail) {

        nameData = itemNames;
        statusData = statusOutput;

        this.screen = screen;

        emailString = myUserEmail;
    }


    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spending_row_view, parent, false);

        SpendingViewHolder holder = new SpendingViewHolder(myView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {

        holder.nameText.setText(nameData.get(position));
        holder.statusText.setText(statusData.get(position));

        holder.returnLayout().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String itemString = holder.getNameText();

                updateUI(itemString);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nameData.size();
    }

    public void changeInfo(ArrayList<String> nameData, ArrayList<String> statusData)
    {
        this.nameData = nameData;
        this.statusData = statusData;
    }

    public void updateUI(String itemString)
    {
        Intent intent = new Intent(this.screen, DeleteSpendingActivity.class);
        intent.putExtra("currItem", itemString);
        intent.putExtra("currUser", emailString);

        this.screen.startActivity(intent);
    }
}
