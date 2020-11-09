package com.example.simbirsoftproject;

import android.annotation.SuppressLint;
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

public class DataAdapterTreners extends RecyclerView.Adapter<DataAdapterTreners.ViewHolderTreners> {

    List<Users> TrenersList;
    LayoutInflater inflater;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    public class ViewHolderTreners extends RecyclerView.ViewHolder{

        TextView nameOfTrener, descriptionOfTrener;
        ImageView imgTrener;

        public ViewHolderTreners(@NonNull View itemView) {
            super(itemView);
            nameOfTrener = (TextView) itemView.findViewById(R.id.nameOfTrener);
            descriptionOfTrener = (TextView) itemView.findViewById(R.id.achievementsOfTrener);
            imgTrener = (ImageView) itemView.findViewById(R.id.imgTreners);
        }
    }

    public DataAdapterTreners(Context context, ArrayList<Users> TrenersList){
        this.TrenersList = TrenersList;
        this.inflater = LayoutInflater.from(context);
        Log.d("logmy", "конструктор адаптера");
    }

    @NonNull
    @Override
    public DataAdapterTreners.ViewHolderTreners onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("logmy", "OnCreateViewHolder");
        View view = inflater.inflate(R.layout.item_trener, parent, false);
        return new DataAdapterTreners.ViewHolderTreners(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DataAdapterTreners.ViewHolderTreners holder, int position) {
        Log.d("logmy", "onBindViewHolder");

        holder.nameOfTrener.setText(TrenersList.get(position).firstName + " " + TrenersList.get(position).lastName);
        storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/" +
                "photoOfUsers").child(TrenersList.get(position).urlPhoto);
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imgTrener.setImageBitmap(bitmap);
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
        if(TrenersList == null) {
            Log.d("logmy", "getItemCount: насчитал 0");
            return 0;
        }
        Log.d("logmy", "getItemCount: насчитал несколько");
        return TrenersList.size();
    }
}
