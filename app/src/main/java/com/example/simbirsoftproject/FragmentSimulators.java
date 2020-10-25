package com.example.simbirsoftproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentSimulators extends Fragment {

    RecyclerView RVSimulators;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulators, container, false);
        RVSimulators = (RecyclerView) view.findViewById(R.id.simulators_recycler);
        RVSimulators.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        return view;
    }
}