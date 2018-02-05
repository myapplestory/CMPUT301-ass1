package com.example.james.csun_subbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James on 2/4/2018.
 *
 */

public class activity_view extends AppCompatActivity {
    Toolbar addToolbar;
    TextView nameInput;
    TextView datetextview;
    TextView amountInput;
    TextView commentInput;

    private Date currentdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        addToolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(addToolbar);

        nameInput = findViewById(R.id.view_nametext);
        datetextview = findViewById(R.id.view_datetext);
        amountInput = findViewById(R.id.view_amounttext);
        commentInput = findViewById(R.id.view_commenttext);

        set_textview();
    }


    public void set_textview(){

        String datestring  = "Date: " + getIntent().getStringExtra("date");
        datetextview.setText(datestring);

        String namestring = "Name: " + getIntent().getStringExtra("name");
        nameInput.setText(namestring);

        String amountstring = "Amount: " + getIntent().getStringExtra("amount");
        amountInput.setText(amountstring);

        String commentstring = "Comment: " + getIntent().getStringExtra("comment");
        commentInput.setText(commentstring);
    }
}
