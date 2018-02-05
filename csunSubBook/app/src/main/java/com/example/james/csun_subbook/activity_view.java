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
 * created on Feb 2 2018
 * @author James Sun
 *
 * this class is in charge of the interface that displays the data of a subscription
 *
 */

public class activity_view extends AppCompatActivity {
    private Toolbar addToolbar;
    private TextView nameInput;
    private TextView datetextview;
    private TextView amountInput;
    private TextView commentInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // initialize toolbar
        addToolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(addToolbar);

        nameInput = findViewById(R.id.view_nametext);
        datetextview = findViewById(R.id.view_datetext);
        amountInput = findViewById(R.id.view_amounttext);
        commentInput = findViewById(R.id.view_commenttext);

        setTextview();
    }


    private void setTextview(){
        // displays the data of the subscription into each of the textviews
        String datestring  = "Date: " + getIntent().getStringExtra("date");
        datetextview.setText(datestring);

        String namestring = "Name: " + getIntent().getStringExtra("name");
        nameInput.setText(namestring);

        String amountstring = "Amount: $" + getIntent().getStringExtra("amount");
        amountInput.setText(amountstring);

        String commentstring = "Comment: " + getIntent().getStringExtra("comment");
        commentInput.setText(commentstring);
    }
}
