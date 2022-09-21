package com.example.gerasimovtaskone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.DayOfWeek;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    String errorMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoToSecAct = (Button) findViewById(R.id.btnNext);
        //слушатель кнопки
        View.OnClickListener oclBtnGoToSecAct = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TwoWindow.class);
                startActivity(intent);
            }
        };
        // обработчик
        btnGoToSecAct.setOnClickListener(oclBtnGoToSecAct);
    }
    public void showTXT(View v) {
        TableLayout List = findViewById(R.id.List);
        List.removeAllViews();

        try {

            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();


            if (connection != null) {
                String query = "Select  ID, Dish, Price, Weight from Menu";

//              запускаем запрос из sql
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {

                    TableRow dbOutputRow = new TableRow(this);
                    dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);

                    TextView button_id = new TextView(this);
                    params.weight = 1.0f;
                    button_id.setLayoutParams(params);
                    button_id.setText(resultSet.getString(1));
                    button_id.setTextSize(12);
                    dbOutputRow.addView(button_id);

                    TextView button_dish = new TextView(this);
                    params.weight = 1.0f;
                    button_dish.setLayoutParams(params);
                    button_dish.setText(resultSet.getString(2));
                    button_dish.setTextSize(12);
                    dbOutputRow.addView(button_dish);

                    TextView button_price = new TextView(this);
                    params.weight = 1.0f;
                    button_price.setLayoutParams(params);
                    button_price.setText(resultSet.getString(3));
                    button_price.setTextSize(12);
                    dbOutputRow.addView(button_price);

                    TextView button_weight = new TextView(this);
                    params.weight = 1.0f;
                    button_weight.setLayoutParams(params);
                    button_weight.setText(resultSet.getString(4));
                    button_weight.setTextSize(12);
                    dbOutputRow.addView(button_weight);
                    List.addView(dbOutputRow);

                }
            } else {
                errorMessage = "Error Connection!";
            }
        } catch (Exception ex) {
            errorMessage = "TRY CONNECTION!";
        }
    }


}
