package com.example.gerasimovtaskone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TwoWindow extends AppCompatActivity {

    EditText Price;
    EditText Dish;
    EditText Weight;
    Button btnClear;

    Connection connection;
    String errorMessage = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, ThreeWindow.class));
                return true;
        }
        //headerView.setText(item.getTitle());
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_window);

        Price = findViewById(R.id.Price);
        Dish = findViewById(R.id.Dish);
        Weight = findViewById(R.id.Weight);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Price.getText().clear();
                Dish.getText().clear();
                Weight.getText().clear();

            }
        });

        //Button btnClear = findViewById(R.id.btnClear);


        Button btnGoToBack = (Button) findViewById(R.id.btnGoToBack);
        //слушатель кнопки
        View.OnClickListener oclbtnGoToBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TwoWindow.this, MainActivity.class);
                startActivity(intent);
            }
        };
        // обработчик
        btnGoToBack.setOnClickListener(oclbtnGoToBack);


    }

    public void inputSQL(View v) {
        TextView Dish = findViewById(R.id.Dish);
        TextView Price = findViewById(R.id.Price);
        TextView Weight = findViewById(R.id.Weight);
        try {

            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();
            if (connection != null) {
                String query = "Insert into Menu(Dish, Price, Weight)" +
                        " Values('" + Dish.getText() + "', '" + Price.getText() + "', '" + Weight.getText() + "')";
//              запускаем запрос из sql
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

            } else {
                errorMessage = "Error Connection!";
            }
        } catch (Exception ex) {
            errorMessage = "TRY CONNECTION!";
        }
    }


}