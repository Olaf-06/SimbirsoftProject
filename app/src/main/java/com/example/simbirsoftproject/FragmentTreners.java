package com.example.simbirsoftproject;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentTreners extends Fragment{

    RecyclerView RVTreners;
    ArrayList<Users> Users = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treners, container, false);
        RVTreners = (RecyclerView) view.findViewById(R.id.treners_recycler);

        RVTreners.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DataAdapterTreners adapter = new DataAdapterTreners(this.getActivity(), Users);
        RVTreners.setAdapter(adapter);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Users UsersClass = document.toObject(Users.class);
                                Log.d("logmy", UsersClass.adminRights);
                                if (UsersClass.adminRights.equals("1")) {
                                    Users.add(new Users(UsersClass.adminRights, UsersClass.firstName, UsersClass.lastName, UsersClass.urlPhoto));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            Log.d("logmy", "прогрузились документы");
                        } else {
                            Log.w("logmy", "Error getting documents.", task.getException());
                        }
                    }
                });
        return view;
    }
}