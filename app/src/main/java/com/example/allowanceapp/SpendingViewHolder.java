package com.example.allowanceapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SpendingViewHolder extends RecyclerView.ViewHolder {

    protected TextView nameText;
    protected TextView statusText;

    public SpendingViewHolder(@NonNull View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.nameTextView);
        statusText = itemView.findViewById(R.id.statusTextView);
    }

    public ConstraintLayout returnLayout()
    {
        return itemView.findViewById(R.id.rowLayout);
    }

    public String getNameText()
    {
        String returnNameText = nameText.getText().toString();

        return returnNameText;
    }
}
