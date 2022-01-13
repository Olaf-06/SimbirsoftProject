package com.example.simbirsoftproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAdapterSimulators extends RecyclerView.Adapter<DataAdapterSimulators.ViewHolderSimulators> {

    List<Data> dataList;
    LayoutInflater inflater;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    public class ViewHolderSimulators extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameOfSimulator, descriptionOfSimulator;
        ImageView imgSimulator, clearItemOfSimulators;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        public ViewHolderSimulators(@NonNull View itemView) {
            super(itemView);
            nameOfSimulator = (TextView) itemView.findViewById(R.id.nameOfSimulator);
            descriptionOfSimulator = (TextView) itemView.findViewById(R.id.descriptionOfSimulator);
            imgSimulator = (ImageView) itemView.findViewById(R.id.imgSimulator);
            clearItemOfSimulators = (ImageView) itemView.findViewById(R.id.clearItemOfSimulators);

            clearItemOfSimulators.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.clearItemOfSimulators) {
                db.collection("simulators").document(dataList.get(getAdapterPosition()).photoID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/" +
                                        "photoOfSimulator").child("simulator" + dataList.get(getAdapterPosition()).photoID);
                                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        removeAt(getAdapterPosition());// File deleted successfully
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.d("logmy", "onFailure: фотка не удалилась");
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("logmy", "Error deleting document", e);
                            }
                        });

            }
        }
    }

    public void removeAt(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    public DataAdapterSimulators(Context context, ArrayList<Data> dataList){
        this.dataList = dataList;
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
        holder.nameOfSimulator.setText(dataList.get(position).name);
        holder.descriptionOfSimulator.setText(dataList.get(position).description);
        storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/" +
                "photoOfSimulator").child("simulator" + dataList.get(position).photoID);
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
    }

    @Override
    public int getItemCount() {
        if(dataList == null) {
            Log.d("logmy", "getItemCount: насчитал 0");
            return 0;
        }
        Log.d("logmy", "getItemCount: насчитал несколько");
        return dataList.size();
    }
}
