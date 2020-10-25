package com.example.simbirsoftproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapterSimulators extends RecyclerView.Adapter<ViewHolderSimulators> {

    List<Simulators> simulatorsList;

    public DataAdapterSimulators(List<Simulators> simulatorsList){
        this.simulatorsList = simulatorsList;
    }

    @NonNull
    @Override
    public ViewHolderSimulators onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simulators, parent, false);
        ViewHolderSimulators viewHolderSimulators = new ViewHolderSimulators(view);
        return viewHolderSimulators;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSimulators holder, int position) {
        holder.nameOfSimulator.setText(simulatorsList.get(position).name);
        holder.descriptionOfSimulator.setText(simulatorsList.get(position).description);
        holder.imgSimulator.setImageBitmap(simulatorsList.get(position).photoID);
    }

    @Override
    public int getItemCount() {
        return simulatorsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
