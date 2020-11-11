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

public class DataAdapterGroupLessons extends RecyclerView.Adapter<DataAdapterGroupLessons.ViewHolderGroupLessons> {

    List<GroupLessons> GroupLessonsList;
    LayoutInflater inflater;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    public class ViewHolderGroupLessons extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameOfGroupLesson, descriptionOfGroupLesson;
        ImageView imgGroupLesson, clearItemOfGroupLesson;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        public ViewHolderGroupLessons(@NonNull View itemView) {
            super(itemView);
            nameOfGroupLesson = (TextView) itemView.findViewById(R.id.nameOfGroupLesson);
            descriptionOfGroupLesson = (TextView) itemView.findViewById(R.id.descriptionOfGroupLesson);
            imgGroupLesson = (ImageView) itemView.findViewById(R.id.imgGroupLessons);
            clearItemOfGroupLesson = (ImageView) itemView.findViewById(R.id.clearItemOfGroupLesson);

            clearItemOfGroupLesson.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.clearItemOfGroupLesson) {
                db.collection("GroupLessons").document(GroupLessonsList.get(getAdapterPosition()).photoID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/" +
                                        "photoOfGroupLesson").child("GroupLesson" + GroupLessonsList.get(getAdapterPosition()).photoID);
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
        GroupLessonsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, GroupLessonsList.size());
    }

    public DataAdapterGroupLessons(Context context, ArrayList<GroupLessons> GroupLessonsList){
        this.GroupLessonsList = GroupLessonsList;
        this.inflater = LayoutInflater.from(context);
        Log.d("logmy", "конструктор адаптера");
    }

    @NonNull
    @Override
    public DataAdapterGroupLessons.ViewHolderGroupLessons onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("logmy", "OnCreateViewHolder");
        View view = inflater.inflate(R.layout.item_group_lesson, parent, false);
        return new DataAdapterGroupLessons.ViewHolderGroupLessons(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterGroupLessons.ViewHolderGroupLessons holder, int position) {
        Log.d("logmy", "onBindViewHolder");
        holder.nameOfGroupLesson.setText(GroupLessonsList.get(position).name);
        holder.descriptionOfGroupLesson.setText(GroupLessonsList.get(position).description);
        storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/" +
                "photoOfGroupLesson").child("GroupLesson" + GroupLessonsList.get(position).photoID);
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imgGroupLesson.setImageBitmap(bitmap);
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
        if(GroupLessonsList == null) {
            Log.d("logmy", "getItemCount: насчитал 0");
            return 0;
        }
        Log.d("logmy", "getItemCount: насчитал несколько");
        return GroupLessonsList.size();
    }
}
