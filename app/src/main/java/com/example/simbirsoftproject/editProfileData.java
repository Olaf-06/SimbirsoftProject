package com.example.simbirsoftproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class editProfileData extends AppCompatActivity implements View.OnClickListener {

    ImageView imgProfile;
    Button btnEdit;
    EditText etFirstName, etLastName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_data);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);

        imgProfile.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Log.d("logmy", "editProfileData создана, все view объявлены");
    }

    @Override
    public void onClick(View view) {
        String firstName = etFirstName.getText().toString(), lastName = etLastName.getText().toString();
        switch (view.getId()) {
            case R.id.btnEdit:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && !firstName.isEmpty() && !lastName.isEmpty()) {
                    String uid = user.getUid();
                    Map<String, Object> userdb = new HashMap<>();
                    userdb.put("firstName", firstName);
                    userdb.put("lastName", lastName);
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
                                    Toast.makeText(editProfileData.this, "Error adding document", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Нужно ввести Имя и Фамилию!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgProfile:
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                Log.d("logmy", "Сейчас будет переход в галерею");
                startActivityForResult(photoPickerIntent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        Log.d("logmy", "В активитиРезалт!");
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgProfile.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}