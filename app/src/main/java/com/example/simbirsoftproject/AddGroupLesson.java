package com.example.simbirsoftproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddGroupLesson extends AppCompatActivity implements View.OnClickListener {

    private Uri imgSrc;
    ImageView imgGroupLesson;
    Button btnEdit;
    EditText etGroupLessonName, etEtGroupLessonDescription;
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int randId = (int) (Math.random() * 100000000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_lesson);
        imgGroupLesson = (ImageView) findViewById(R.id.imgProfile);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        etGroupLessonName = (EditText) findViewById(R.id.etGroupLessonName);
        etEtGroupLessonDescription = (EditText) findViewById(R.id.etGroupLessonDescription);

        imgGroupLesson.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Log.d("logmy", "editProfileData создана, все view объявлены");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEdit:
                String name = etGroupLessonName.getText().toString(), description = etEtGroupLessonDescription.getText().toString();
                Log.d("logmy", "Загрузили фото");
                if (!name.isEmpty() && !description.isEmpty()) {
                    Map<String, Object> userdb = new HashMap<>();
                    userdb.put("name", name);
                    userdb.put("description", description);
                    userdb.put("photoID", "" + randId);
                    Log.d("logmy", "Cей час будем добавлять документ в коллекцию");
                    db.collection("GroupLessons").document("" + randId)
                            .set(userdb)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(AddGroupLesson.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddGroupLesson.this, "Error adding document", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Нужно ввести Название и Описание!", Toast.LENGTH_SHORT).show();
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
                        imgSrc = imageUri;
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgGroupLesson.setImageBitmap(selectedImage);
                        StorageReference riversRef = mStorageRef.child("photoOfGroupLesson/" + "GroupLesson" + randId);
                        riversRef.putFile(imgSrc)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}