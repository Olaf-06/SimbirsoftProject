package com.example.simbirsoftproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;

public class FragmentTimetable extends Fragment {

    TextView txtMonday, txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday, txtSunday;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        txtMonday = (TextView) view.findViewById(R.id.txtInfoMonday);
        txtTuesday = (TextView) view.findViewById(R.id.txtInfoTuesday);
        txtWednesday = (TextView) view.findViewById(R.id.txtInfoWednesday);
        txtThursday = (TextView) view.findViewById(R.id.txtInfoThursday);
        txtFriday = (TextView) view.findViewById(R.id.txtInfoFriday);
        txtSaturday = (TextView) view.findViewById(R.id.txtInfoSaturday);
        txtSunday = (TextView) view.findViewById(R.id.txtInfoSunday);
        setHasOptionsMenu(true);

        db.collection("Timetable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timetable timetable = document.toObject(Timetable.class);
                                txtMonday.setText(timetable.getMonday());
                                txtTuesday.setText(timetable.getTuesday());
                                txtWednesday.setText(timetable.getWednesday());
                                txtThursday.setText(timetable.getThursday());
                                txtFriday.setText(timetable.getFriday());
                                txtSaturday.setText(timetable.getSaturday());
                                txtSunday.setText(timetable.getSunday());
                            }
                        } else {
                        }
                    }
                });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.timetable_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_edit:
                Intent intent = new Intent(getActivity(), editTimetable.class);
                intent.putExtra("monday", txtMonday.getText().toString());
                intent.putExtra("tuesday", txtTuesday.getText().toString());
                intent.putExtra("wednesday", txtWednesday.getText().toString());
                intent.putExtra("thursday", txtThursday.getText().toString());
                intent.putExtra("friday", txtFriday.getText().toString());
                intent.putExtra("saturday", txtSaturday.getText().toString());
                intent.putExtra("sunday", txtSunday.getText().toString());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}