package com.example.gerasimovtaskone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FourWindow extends AppCompatActivity {

    EditText Price;
    EditText Dish;
    EditText Weight;
    Button btnClear;

    int i;
    Connection connection;
    String errorMessage = "";

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult( // вывов диалогового окна
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    Bitmap bitmap = null;
                    ImageView imageView = (ImageView) findViewById(R.id.image);
                    if (result.getResultCode() == Activity.RESULT_OK) { // если выбрали фото в диалоговом окне, выполняем
                        Uri selectedImage = result.getData().getData();
                        try
                        {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(null); // замена существующей картинки
                        imageView.setImageBitmap(bitmap);

                        Button deletePicture = findViewById(R.id.deletePhoto);

                        String varcharPicture = BitMapToString(bitmap); // конвертим картинку в стринг, чтобы потом закинуть ее в бд
                        addPicture(varcharPicture);
                    }
                }
            });
    public String BitMapToString(Bitmap bitmap) // метод для конвертации в стринг
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void addPicture(String varcharPicture)
    {
        try
        {
            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();
            if(connection != null) {
                String query;
                if(varcharPicture == ""){
                    query = "update menu set picture = null where ID = " + i;
                }
                else{
                    query = "update menu set picture = '" + varcharPicture + "' where ID = " + i;
                }
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "При добавление картинки возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }
    public void deletePicture(View v)
    {
        ImageView picture = (ImageView) findViewById(R.id.image);
        picture.setImageBitmap(null);
        addPicture("");
    }

    public void Picture(View v){
        Intent photo = new Intent(Intent.ACTION_PICK);
        photo.setType("image/*"); // ищем любую картинку с типом jpg png
        someActivityResultLauncher.launch(photo);


    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public void UpdateTable(View v) {
        TextView Dish = findViewById(R.id.Dish);
        TextView Price = findViewById(R.id.Price);
        TextView Weight = findViewById(R.id.Weight);
        try {
            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();

            if (connection != null) {
                String query = "Update Menu set Dish = '"
                        + Dish.getText() + "', Price = '" + Price.getText() +
                        "', Weight = '" + Weight.getText() + "' where ID = " + i;
//              запускаем запрос из sql
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                Toast.makeText(this, "Успешное добавление", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Не получилось изменить, попробуйте еще раз", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            errorMessage = "TRY CONNECTION!";
        }


    }

    public void DeleteRow(View v) {
        try {
            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();

            if (connection != null) {
                String query = "delete from Menu where ID = " + i;
//              запускаем запрос из sql
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                Toast.makeText(this, "Запись из меню успешно удалена!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FourWindow.this, ThreeWindow.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Не получилось удалить, попробуйте еще раз", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            errorMessage = "TRY CONNECTION!";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_window);

        Price = findViewById(R.id.Price);
        Dish = findViewById(R.id.Dish);
        Weight = findViewById(R.id.Weight);
        btnClear = findViewById(R.id.btnClear);

        // "памятка", если сотрутся поля. выводим название текствью
        Price.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                Price.setHint("");
            else
                Price.setHint("Price");
        });
        Dish.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                Dish.setHint("");
            else
                Dish.setHint("Dish");
        });
        Weight.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                Weight.setHint("");
            else
                Weight.setHint("Weight");
        });


        //Button btnClear = findViewById(R.id.btnClear);
        Button btnGoToBack = (Button) findViewById(R.id.btnGoToBack);
        //слушатель кнопки
        View.OnClickListener oclbtnGoToBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FourWindow.this, ThreeWindow.class);
                startActivity(intent);
            }
        };
        // обработчик
        btnGoToBack.setOnClickListener(oclbtnGoToBack);
        Bundle arguments = getIntent().getExtras();
        i = arguments.getInt("key");
        ChangeForDB();



    }

    public void ChangeForDB() {
        TextView Dish = findViewById(R.id.Dish);
        TextView Price = findViewById(R.id.Price);
        TextView Weight = findViewById(R.id.Weight);
        try {
            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();

            if (connection != null) {
                String query = "select * from Menu where ID = " + i;
//              запускаем запрос из sql
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {

                    ImageView image = findViewById(R.id.image);
                    Dish.setText(resultSet.getString(2));
                    Price.setText(resultSet.getString(3));
                    Weight.setText(resultSet.getString(4));
                    if(resultSet.getString(5) == null)
                    {
                        image.setImageResource(R.drawable.nonephoto);
                    }
                    else{
                        Bitmap bitmap = StringToBitMap(resultSet.getString(5));
                        image.setImageBitmap(bitmap);
                    }
                }

            } else {
                errorMessage = "Error Connection!";
            }
        } catch (Exception ex) {
            errorMessage = "TRY CONNECTION!";
        }


    }

}
