package com.example.simbirsoftproject;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddGroupLesson extends AppCompatActivity implements View.OnClickListener {

    private Uri imgSrc;
    ImageView imgGroupLesson;
    Button btnEdit;
    EditText etGroupLessonName, etEtGroupLessonDescription;
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int randId = (int) (Math.random() * 100000000);
    EditText etStartTime, etEndTime, etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_lesson);
        imgGroupLesson = (ImageView) findViewById(R.id.imgProfile);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        etGroupLessonName = (EditText) findViewById(R.id.etGroupLessonName);
        etEtGroupLessonDescription = (EditText) findViewById(R.id.etGroupLessonDescription);
        etStartTime = (EditText) findViewById(R.id.etStartOfTime);
        etEndTime = (EditText) findViewById(R.id.etEndOfTime);
        etDate = (EditText) findViewById(R.id.etDate);

        imgGroupLesson.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Log.d("logmy", "editProfileData создана, все view объявлены");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEdit:

                String name = etGroupLessonName.getText().toString(),
                        description = etEtGroupLessonDescription.getText().toString(),
                        startTime = etStartTime.getText().toString(),
                        endTime = etEndTime.getText().toString(),
                        date = etDate.getText().toString();

                Pattern pattern = Pattern.compile("^[0-2][0-9]:[0-5][0-9]$");

                Boolean boolStartTime = pattern.matcher(startTime).matches(),
                        boolEndTime = pattern.matcher(endTime).matches();

                pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)" +
                        "(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|" +
                        "^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|" +
                        "[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|" +
                        "^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4" +
                        "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

                Boolean boolDate = pattern.matcher(date).matches();

                Log.d("logmy", "Вне условия");

                if (boolEndTime && boolStartTime && boolDate) {
                    Log.d("logmy", "Внутри первого условия");
                    if (!name.isEmpty() && !description.isEmpty() &&
                            !startTime.isEmpty() && !endTime.isEmpty()) {

                        String day = "", month = "", year = "";

                        int j = 0;

                        for (int i = 0; i < date.length(); i++) {
                            if (date.charAt(i) == '.') {
                                j+=2;
                                Log.d("logmy", i + " " + date.charAt(i));
                                break;
                            }
                            Log.d("logmy", i + " " + date.charAt(i));
                            day += date.charAt(i);
                            j = i;
                        }

                        for (int i = j; i < date.length(); i++) {
                            if (date.charAt(i) == '.') {
                                j+=2;
                                break;
                            }
                            Log.d("logmy", "во втором цикле");
                            month += date.charAt(i);
                            j = i;
                        }

                        for (int i = j; i < date.length(); i++) {
                            year += date.charAt(i);
                        }

                        Log.d("logmy", "Внутри второго условия");
                        Map<String, Object> userdb = new HashMap<>();
                        userdb.put("name", name);
                        userdb.put("description", description);
                        userdb.put("photoID", "" + randId);
                        userdb.put("startTime", startTime);
                        userdb.put("endTime", endTime);
                        userdb.put("day", day);
                        userdb.put("month", month);
                        userdb.put("year", year);
                        Log.d("logmy", "Cей час будем добавлять документ в коллекцию");
                        db.collection("GroupLessons").document("" + randId)
                                .set(userdb)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(AddGroupLesson.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddGroupLesson.this,
                                                "Error adding document",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Log.d("logmy", "Внутри первого else");
                        Toast.makeText(AddGroupLesson.this,
                                "Нужно ввести Название и Описание!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("logmy", "Внутри второго else");
                    Toast.makeText(AddGroupLesson.this,
                            "Введите время в формате ЧЧ:ММ и дату в формате ДД.ММ.ГГГГ!",
                            Toast.LENGTH_SHORT).show();
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
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
//Получаем URI изображения, преобразуем его в Bitmap
//объект и отображаем в элементе ImageView нашего интерфейса:
                    final Uri imageUri = imageReturnedIntent.getData();
                    imgSrc = imageUri;
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgGroupLesson.setImageBitmap(selectedImage);
                    StorageReference riversRef = mStorageRef.child("photoOfGroupLesson/" +
                            "GroupLesson" + randId);
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