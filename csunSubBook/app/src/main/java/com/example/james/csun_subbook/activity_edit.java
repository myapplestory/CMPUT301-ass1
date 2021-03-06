package com.example.james.csun_subbook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

/**
 * created on Feb 2 2018
 * @author James Sun
 *
 * this class is in charge of the interface where the user can edit the data of a existing subscription
 *
 */

public class activity_edit extends AppCompatActivity {
    private static final String FILENAME = "scrub_list.sav";    // save file destination
    private Toolbar editToolbar;
    private EditText nameInput;             // initially declare some varable types
    private TextView datetextview;
    private EditText amountInput;
    private EditText commentInput;
    private ArrayList<Subscription> sublist;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Date currentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // initialize toolbar
        editToolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(editToolbar);

        // set each textview and edittext on the layout to each name
        nameInput = findViewById(R.id.edit_nametext);
        datetextview = findViewById(R.id.edit_datetext);
        amountInput = findViewById(R.id.edit_amounttext);
        commentInput = findViewById(R.id.edit_commenttext);

        sublist = new ArrayList<>();

        try {
            setTextview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // the following two functions are in charge of the calendar fragment
        // where the user can select a date for a subscription
        datetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog;
                dialog = new DatePickerDialog(activity_edit.this,
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
    protected void onStart () {
        super.onStart();
        loadFromFile();
    }

    private void setTextview()throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String datestring = getIntent().getStringExtra("date");
        currentdate = dateFormat.parse(datestring);
        datetextview.setText(datestring);

        String namestring = getIntent().getStringExtra("name");
        nameInput.setText(namestring);

        String amountstring = getIntent().getStringExtra("amount");
        amountInput.setText(amountstring);

        String commentstring = getIntent().getStringExtra("comment");
        commentInput.setText(commentstring);
    }


    // function that displays the add option on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_option_menu, menu);
        return true;
    }

    // function that does stuff when add is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_change) {
            String name = nameInput.getText().toString();
            String amountstring = amountInput.getText().toString();
            String comment = commentInput.getText().toString();
            Float amountfloat = 0.0f;

            // checks if textviews contains data following the requirements
            // if so, add subscription details to a new Subscription and add to sublist and savefile
            // and go back to MainActivity
            if (Objects.equals(name, "") || Objects.equals(amountstring, "")){
                Toast.makeText(activity_edit.this,
                        "One or more of the required fields are not filled", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else if (name.length() > 20){
                Toast.makeText(activity_edit.this,
                        "Name field longer than 20 characters", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else if (comment.length() > 30){
                Toast.makeText(activity_edit.this,
                        "Comment field longer than 30 characters", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            try {
                amountfloat = Float.parseFloat(amountstring);
            } catch (Exception exception){
                Toast.makeText(activity_edit.this,
                        "Incorrect value in amount field", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            Subscription subscription = new Subscribe(name, currentdate, amountfloat, comment);

            Integer index = Integer.parseInt(getIntent().getStringExtra("position"));
            sublist.remove(sublist.get(index));
            sublist.add(index, subscription);

            saveInFile();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Toast.makeText(activity_edit.this, "Subscription changed", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // load/savefromfile gotten from lonely twitter
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
