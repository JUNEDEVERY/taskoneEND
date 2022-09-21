package com.example.gerasimovtaskone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                    Dish.setText(resultSet.getString(2));
                    Price.setText(resultSet.getString(3));
                    Weight.setText(resultSet.getString(4));
                }

            } else {
                errorMessage = "Error Connection!";
            }
        } catch (Exception ex) {
            errorMessage = "TRY CONNECTION!";
        }


    }

}
