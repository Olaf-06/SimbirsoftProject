package com.example.simbirsoftproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editTimetable extends AppCompatActivity implements View.OnClickListener {

    EditText etMonday, etTuesday, etWednesday, etThursday, etFriday, etSaturday, etSunday;

    Button btnTimetableEdit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timetable);
        etMonday = (EditText) findViewById(R.id.etEditMonday);
        etTuesday = (EditText) findViewById(R.id.etEditTuesday);
        etWednesday = (EditText) findViewById(R.id.etEditWednesday);
        etThursday = (EditText) findViewById(R.id.etEditThursday);
        etFriday = (EditText) findViewById(R.id.etEditFriday);
        etSaturday = (EditText) findViewById(R.id.etEditSaturday);
        etSunday = (EditText) findViewById(R.id.etEditSunday);

        btnTimetableEdit = (Button) findViewById(R.id.editTimetableConfirm);
        btnTimetableEdit.setOnClickListener(this);

        etMonday.setText(getIntent().getExtras().getString("monday"));
        etTuesday.setText(getIntent().getExtras().getString("tuesday"));
        etWednesday.setText(getIntent().getExtras().getString("wednesday"));
        etThursday.setText(getIntent().getExtras().getString("thursday"));
        etFriday.setText(getIntent().getExtras().getString("friday"));
        etSaturday.setText(getIntent().getExtras().getString("saturday"));
        etSunday.setText(getIntent().getExtras().getString("sunday"));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editTimetableConfirm) {
            Map<String, Object> timetable = new HashMap<>();
            timetable.put("monday", etMonday.getText().toString());
            timetable.put("tuesday", etTuesday.getText().toString());
            timetable.put("wednesday", etWednesday.getText().toString());
            timetable.put("thursday", etThursday.getText().toString());
            timetable.put("friday", etFriday.getText().toString());
            timetable.put("saturday", etSaturday.getText().toString());
            timetable.put("sunday", etSunday.getText().toString());
            db.collection("Timetable").document("timetable")
                    .set(timetable)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(editTimetable.this, "Расписание изменено!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(editTimetable.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }
}