package com.example.gerasimovtaskone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class ThreeWindow extends AppCompatActivity {

    Connection connection;
    String errorMessage = "";

    public void SearchMenu(View v){
        String query = "select * from Menu";
        Spinner spinner1 = findViewById(R.id.spinner1);
        EditText editText = findViewById(R.id.editText1);

        if(spinner1.getSelectedItem().toString().equals("")){
            if(!editText.getText().toString().equals("")){
                query = "select *from Menu where Dish = '" + editText.getText().toString() + "'" ;
            }

        }

        else{
            if(!editText.getText().toString().equals("")){
                query = "select * from Menu where Dish = '" + editText.getText().toString() +  "' order by " + spinner1.getSelectedItem().toString();
            }
            else{
                query = "select * from Menu order by " + spinner1.getSelectedItem().toString();
            }

        }

        UpdateTable(query);
    }

public void Clear(View v){

    Spinner spinner1 = findViewById(R.id.spinner1);
    EditText editText = findViewById(R.id.editText1);

    spinner1.setSelection(0);
    editText.setText("");
    UpdateTable("select * from Menu");


}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_window);
        UpdateTable("select * from Menu");

        Button btnGoToBack = (Button) findViewById(R.id.btnGoToBack);
        //слушатель кнопки
        View.OnClickListener oclbtnGoToBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThreeWindow.this, TwoWindow.class);
                startActivity(intent);
            }
        };
        // обработчик
        btnGoToBack.setOnClickListener(oclbtnGoToBack);
    }

    public void UpdateTable(String query) {
        TableLayout dbOutput = findViewById(R.id.dbOutput);
        dbOutput.removeAllViews();
        try {
            DBhelper dBhelper = new DBhelper();
            connection = dBhelper.connectionClass();
            if (connection != null) {
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
                    button_id.setTextSize(10);
                    dbOutputRow.addView(button_id);

                    TextView button_dish = new TextView(this);
                    params.weight = 1.0f;
                    button_dish.setLayoutParams(params);
                    button_dish.setText(resultSet.getString(2));
                    button_dish.setTextSize(13);
                    dbOutputRow.addView(button_dish);

                    TextView button_price = new TextView(this);
                    params.weight = 1.0f;
                    button_price.setLayoutParams(params);
                    button_price.setText(resultSet.getString(3));
                    button_price.setTextSize(13);
                    dbOutputRow.addView(button_price);

                    TextView button_weight = new TextView(this);
                    params.weight = 1.0f;
                    button_weight.setLayoutParams(params);
                    button_weight.setText(resultSet.getString(4));
                    button_weight.setTextSize(13);
                    dbOutputRow.addView(button_weight);

                    Button deleteBtn = new Button(this);
                    deleteBtn.setOnClickListener(this::NextToNewWindowWithButton);
                    params.weight = 1.0f;
                    deleteBtn.setLayoutParams(params);
                    deleteBtn.setText("Изменить запись");
                    deleteBtn.setTextSize(11);
                    dbOutputRow.addView(deleteBtn);
                    dbOutput.addView(dbOutputRow);
                }
            } else {
                errorMessage = "Check Connection";
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Вывод данных завершился ошибкой. Попробуйте еще раз", Toast.LENGTH_LONG).show();
        }

    }
    public void NextToNewWindowWithButton(View v) {
        TableRow tableRow = (TableRow) v.getParent();
        TextView textView = (TextView) tableRow.getChildAt(0);
        String idLine = textView.getText().toString();
        Intent intent = new Intent(ThreeWindow.this, FourWindow.class);
        // используем ключ-значение. с бандл мы можем передать данные с одного активити на другое
        Bundle b = new Bundle();
        b.putInt("key", Integer.parseInt(idLine));
        intent.putExtras(b);
        startActivity(intent);
        finish();

    }


}
