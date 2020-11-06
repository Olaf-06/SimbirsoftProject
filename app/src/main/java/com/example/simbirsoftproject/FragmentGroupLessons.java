package com.example.simbirsoftproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentGroupLessons extends Fragment implements View.OnClickListener {

    RecyclerView RVGroupLessons;
    ArrayList<GroupLessons> GroupLessons = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_lessons, container, false);
        RVGroupLessons = (RecyclerView) view.findViewById(R.id.group_lessons_recycler);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabGroupLessons);
        floatingActionButton.setOnClickListener(this);

        RVGroupLessons.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DataAdapterGroupLessons adapter = new DataAdapterGroupLessons(this.getActivity(), GroupLessons);
        RVGroupLessons.setAdapter(adapter);

        db.collection("GroupLessons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GroupLessons GroupLessonsClass = document.toObject(GroupLessons.class);
                                GroupLessons.add(new GroupLessons(GroupLessonsClass.name, GroupLessonsClass.description, GroupLessonsClass.photoID));
                                adapter.notifyDataSetChanged();
                            }
                            Log.d("logmy", "прогрузились документы");
                        } else {
                            Log.w("logmy", "Error getting documents.", task.getException());
                        }
                    }
                });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabGroupLessons:
                Intent intent = new Intent(FragmentGroupLessons.this.getContext(), AddGroupLesson.class);
                startActivity(intent);
                break;
        }
    }
}