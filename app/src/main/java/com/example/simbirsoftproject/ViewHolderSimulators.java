package com.example.simbirsoftproject;

import android.os.TestLooperManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderSimulators extends RecyclerView.ViewHolder {

    TextView nameOfSimulator, descriptionOfSimulator;
    ImageView imgSimulator;

    public ViewHolderSimulators(@NonNull View itemView) {
        super(itemView);
        nameOfSimulator = (TextView) itemView.findViewById(R.id.nameOfSimulator);
        descriptionOfSimulator = (TextView) itemView.findViewById(R.id.descriptionOfSimulator);
        imgSimulator = (ImageView) itemView.findViewById(R.id.imgSimulator);
    }
}
