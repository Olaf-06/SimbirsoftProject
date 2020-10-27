package com.example.simbirsoftproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdapterSimulators extends RecyclerView.Adapter<ViewHolderSimulators> {

    List<Simulators> simulatorsList;
    LayoutInflater inflater;

    public DataAdapterSimulators(Context context, ArrayList<Simulators> simulatorsList){
        this.simulatorsList = simulatorsList;
        this.inflater = LayoutInflater.from(context);
        Log.d("logmy", "конструктор адаптера");
    }

    @NonNull
    @Override
    public ViewHolderSimulators onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("logmy", "OnCreateViewHolder");
        View view = inflater.inflate(R.layout.item_simulators, parent, false);
        return new ViewHolderSimulators(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderSimulators holder, int position) {
        Log.d("logmy", "onBindViewHolder");
        holder.nameOfSimulator.setText(simulatorsList.get(position).name);
        holder.descriptionOfSimulator.setText(simulatorsList.get(position).description);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/" +
                "photoOfUsers").child("simulator" + simulatorsList.get(position).photoID);
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imgSimulator.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("logmy", "onFailure: фотка не загрузилась ");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("logmy", "onBindViewHolder: ставлю на вьюшки значения");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(simulatorsList == null) {
            Log.d("logmy", "getItemCount: насчитал 0");
            return 1;
        }
        Log.d("logmy", "getItemCount: насчитал несколько");
        return simulatorsList.size();
    }
}
