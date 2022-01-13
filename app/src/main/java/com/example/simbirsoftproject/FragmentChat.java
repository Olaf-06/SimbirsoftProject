package com.example.simbirsoftproject;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FragmentChat extends Fragment implements View.OnClickListener {

    private FirebaseListAdapter<Message> adapter;
    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(this);

        ListView listMessages = (ListView) view.findViewById(R.id.listView);
        adapter = new FirebaseListAdapter<Message>(getActivity(), Message.class, R.layout.item_message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView textMessage, autor, timeMessage;
                textMessage = (TextView) v.findViewById(R.id.tvMessage);
                autor = (TextView) v.findViewById(R.id.tvUser);
                timeMessage = (TextView) v.findViewById(R.id.tvTime);

                textMessage.setText(model.getTextMessage());
                autor.setText(model.getAutor());
                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        EditText input = (EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.editText);
        FirebaseDatabase.getInstance().getReference().push()
                .setValue(new Message(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
        input.setText("");
    }
}
