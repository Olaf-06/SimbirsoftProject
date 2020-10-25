package com.example.simbirsoftproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapterChat extends RecyclerView.Adapter<ViewHolderChat> {

    ArrayList<String> messages;

    LayoutInflater inflater;

    public DataAdapterChat(Context context, ArrayList<String> messages) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolderChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChat holder, int position) {

        String msg = messages.get(position);
        holder.message.setText(msg);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
