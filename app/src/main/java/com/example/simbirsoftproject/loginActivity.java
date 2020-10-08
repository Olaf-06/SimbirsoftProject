package com.example.simbirsoftproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "mySharedPreferences";
    public static final String APP_PREFERENCES_NAME = "username";
    public static final String APP_PREFERENCES_SETTINGS_OF_ENTRY = "entry";

    SharedPreferences mySharedPreferences;

    private FirebaseAuth mAuth;
    EditText etLogin, etPassword;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    public void onClick(View view) {
        final String nickName = etLogin.getText().toString(), password = etPassword.getText().toString();
        if(!nickName.isEmpty() && !password.isEmpty()){
            switch (view.getId()) {
                case R.id.btnLogin:
                    mAuth.signInWithEmailAndPassword(nickName, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                                        editor.putBoolean(APP_PREFERENCES_SETTINGS_OF_ENTRY, false);
                                        editor.putString(APP_PREFERENCES_NAME, etLogin.getText().toString());
                                        editor.apply();
                                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        updateUI(null);
                                        Toast.makeText(loginActivity.this, "Вы ввели неправильный Email И/ИЛИ пароль!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    break;
                case R.id.btnRegister:
                    mAuth.createUserWithEmailAndPassword(nickName, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        Toast.makeText(loginActivity.this, "Регистрация прошла успешно! Попробуйте войти!", Toast.LENGTH_LONG).show();
                                    } else {
                                        updateUI(null);
                                        Toast.makeText(loginActivity.this, "Для регистрации используется email адрес, который не был ещё зарегистрирован", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    break;
            }
        } else {
            Toast.makeText(this, "Поля не заполнены!", Toast.LENGTH_SHORT).show();
        }
    }
}