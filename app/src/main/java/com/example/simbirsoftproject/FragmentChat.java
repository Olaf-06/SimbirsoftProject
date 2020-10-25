package com.example.simbirsoftproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentChat extends Fragment implements View.OnClickListener {

    public static final String TAG = "messages";
    EditText edMessage;
    Button btnSendMessage;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    RecyclerView RVMessages;

    ArrayList<String> messages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        edMessage = (EditText)view.findViewById(R.id.message_input);
        btnSendMessage = (Button) view.findViewById(R.id.send_message_b);
        RVMessages = (RecyclerView)view.findViewById(R.id.messages_recycler);

        RVMessages.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DataAdapterChat dataAdapter = new DataAdapterChat(this.getActivity(), messages);
        RVMessages.setAdapter(dataAdapter);

        btnSendMessage.setOnClickListener(FragmentChat.this);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg = snapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                RVMessages.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_message_b:
                String str = edMessage.getText().toString();
                if (str.isEmpty()) {
                    Toast.makeText(getActivity(), "Введите сообщение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (str.length() > 100) {
                    Toast.makeText(getActivity(), "Текст сообщения должен быть состоять меньше, чем из 100-а символов!", Toast.LENGTH_SHORT).show();
                }
                myRef.push().setValue(str);
                edMessage.setText("");
                break;
        }
    }
}