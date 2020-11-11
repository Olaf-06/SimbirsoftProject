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

public class FragmentShares extends Fragment implements View.OnClickListener {

    RecyclerView RVShares;
    ArrayList<Shares> Shares = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shares, container, false);
        RVShares = (RecyclerView) view.findViewById(R.id.shares_recycler);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabShares);
        floatingActionButton.setOnClickListener(this);

        RVShares.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DataAdapterShares adapter = new DataAdapterShares(this.getActivity(), Shares);
        RVShares.setAdapter(adapter);

        db.collection("Shares")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Shares SharesClass = document.toObject(Shares.class);
                                Shares.add(new Shares(SharesClass.name, SharesClass.description, SharesClass.photoID));
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
        switch (view.getId()) {
            case R.id.fabShares:
                Intent intent = new Intent(FragmentShares.this.getContext(), AddShare.class);
                startActivity(intent);
                break;
        }
    }
}