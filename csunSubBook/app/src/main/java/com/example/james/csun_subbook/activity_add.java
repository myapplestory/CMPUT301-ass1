package com.example.james.csun_subbook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class activity_add extends AppCompatActivity {
    private static final String FILENAME = "scrub_list.sav";
    Toolbar addToolbar;
    EditText nameInput;
    TextView datetextview;
    EditText amountInput;
    EditText commentInput;

    private ArrayList<Subscription> sublist;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Date currentdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addToolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(addToolbar);

        nameInput = findViewById(R.id.add_nametext);
        datetextview = findViewById(R.id.add_datetext);
        amountInput = findViewById(R.id.add_amounttext);
        commentInput = findViewById(R.id.add_commenttext);

        sublist = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        currentdate = new Date();
        datetextview.setText(dateFormat.format(currentdate));


        datetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog;
                dialog = new DatePickerDialog(activity_add.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar result = new GregorianCalendar();
                result.set(year,month,day,0,0,0);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                currentdate = result.getTime();
                datetextview.setText(dateFormat.format(currentdate));
            }
        };
    }



    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    // function that displays the add option on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    // function that does stuff when add is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            String name = nameInput.getText().toString();
            String amountstring = amountInput.getText().toString();
            String comment = commentInput.getText().toString();
            Float amountfloat = 0.0f;

            if (Objects.equals(name, "") || Objects.equals(amountstring, "")){
                Toast.makeText(activity_add.this,
                        "One or more of the required fields are not filled", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else if (name.length() > 20){
                Toast.makeText(activity_add.this,
                        "Name field longer than 20 characters", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else if (comment.length() > 30){
                Toast.makeText(activity_add.this,
                        "Comment field longer than 30 characters", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            try {
                amountfloat = Float.parseFloat(amountstring);
            } catch (Exception exception){
                Toast.makeText(activity_add.this,
                        "Incorrect value in amount field", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            Subscription subscription = new Subscribe(name, currentdate, amountfloat, comment);
            sublist.add(subscription);
            saveInFile();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Toast.makeText(activity_add.this, "Subscription added", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Subscribe>>(){}.getType();
            sublist = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            sublist = new ArrayList<>();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(sublist, out);

            out.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
