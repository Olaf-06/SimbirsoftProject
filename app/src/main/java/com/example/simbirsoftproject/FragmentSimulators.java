package com.example.simbirsoftproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;

public class FragmentSimulators extends Fragment implements View.OnClickListener {

    RecyclerView RVSimulators;
    ArrayList<Simulators> simulators = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulators, container, false);
        RVSimulators = (RecyclerView) view.findViewById(R.id.simulators_recycler);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        RVSimulators.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DataAdapterSimulators adapter = new DataAdapterSimulators(this.getActivity(), simulators);
        RVSimulators.setAdapter(adapter);

        db.collection("simulators")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Simulators simulatorsClass = document.toObject(Simulators.class);
                                simulators.add(new Simulators(simulatorsClass.name, simulatorsClass.description, simulatorsClass.photoID));
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
            case R.id.fab:
                Intent intent = new Intent(FragmentSimulators.this.getContext(), AddSimulator.class);
                startActivity(intent);
                break;
        }
    }
}