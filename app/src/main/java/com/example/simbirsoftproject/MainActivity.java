package com.example.simbirsoftproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "mySharedPreferences";
    public static final String APP_PREFERENCES_NAME = "username";
    public static final String APP_PREFERENCES_SETTINGS_OF_ENTRY = "entry";
    public static final String APP_PREFERENCES_SETTINGS_OF_REGISTRATION = "reg";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences mySharedPreferences;
    private Boolean entryBool, registration;
    private String nickName;
    ImageView imgInMainMenu;
    FirebaseUser user;
    TextView tvHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("logMy","В методе OnCreate");
        super.onCreate(savedInstanceState);
        //Log.d("logMy","Скоро поставится SetContentView");
        setContentView(R.layout.activity_main);
        //Log.d("logMy","Запуск main activity");
        mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        entryBool = mySharedPreferences.getBoolean(APP_PREFERENCES_SETTINGS_OF_ENTRY, true);
        nickName = mySharedPreferences.getString(APP_PREFERENCES_NAME, "");
        registration = mySharedPreferences.getBoolean(APP_PREFERENCES_SETTINGS_OF_REGISTRATION, false);
        if (entryBool) {
            Intent intent = new Intent(MainActivity.this, loginActivity.class);
            startActivity(intent);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragmentAchievements,
                R.id.fragmentChat,
                R.id.fragmentGroupLessons,
                R.id.fragmentMain,
                R.id.fragmentShares,
                R.id.fragmentSimulators,
                R.id.fragmentTheory,
                R.id.fragmentTimetable,
                R.id.fragmentTreners)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View header = navigationView.getHeaderView(0);
        tvHeaderEmail = (TextView) header.findViewById(R.id.tvHeaderEmail);
        tvHeaderEmail.setText(nickName);
        Button btnHeaderExit = (Button) header.findViewById(R.id.btnHeaderExit);
        btnHeaderExit.setOnClickListener(this);

        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && registration) {
                String uid = user.getUid();

                Map<String, Object> userdb = new HashMap<>();
                userdb.put("adminRights", "0");
                userdb.put("firstName", "");
                userdb.put("lastName", "");
                userdb.put("urlPhoto", "");
                Log.d("logmy", "Аутентификация прошла и сейчас будем добавлять документ в коллекцию");
                db.collection("users").document(uid)
                        .set(userdb)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                            }
                        });
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_SETTINGS_OF_REGISTRATION, false);
                editor.apply();
            }
            imgInMainMenu = (ImageView) header.findViewById(R.id.imgInMainMenu);
            imgInMainMenu.setOnClickListener(this);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://simbirsoftproject.appspot.com/photoOfUsers").child(user.getUid());
            try {
                final File localFile = File.createTempFile("images", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imgInMainMenu.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e ) {}
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users users = documentSnapshot.toObject(Users.class);
                    if(!users.getFirstName().isEmpty() && !users.getLastName().isEmpty()){
                        tvHeaderEmail.setText(users.getFirstName() + " " + users.getLastName());
                    }
                }
            });
            Log.d("logmy", "onCreate");
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(MainActivity.this, loginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHeaderExit:
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_SETTINGS_OF_ENTRY, true);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                break;
            case R.id.imgInMainMenu:
                Intent intent1 = new Intent(MainActivity.this, editProfileData.class);
                startActivity(intent1);
                break;
        }
    }
}
