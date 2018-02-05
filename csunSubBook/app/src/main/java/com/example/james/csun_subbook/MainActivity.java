package com.example.james.csun_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Locale;

/**
 * MainActivity which is in charge of everything that happens in the main screen with the list view
 * and total amount
 * listview displays each subscription
 * textview displays the total amount of each subscription
 *
 * app created on Feb 2 2018
 * @author James Sun
 * collaborators: none
 * references:
 *     listview long hold context menu functionality gotten from
 *     https://www.youtube.com/watch?v=wSCIuIbS-
 *     and https://www.youtube.com/watch?v=Pq9YQl0nfEk
 *     add button in toolbar and functionalities gotten from
 *     https://www.youtube.com/watch?v=EZ-sNN7UWFU
 *     calendar fragment popup in edit activity and add activity gotten from
 *     https://www.youtube.com/watch?v=Pq9YQl0nfEk
 *
 *     passing data from activity to another activity using intent and putExtra gotten from
 *     https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
 *
 *     Subscription object and idea as well as the savefile loadfile function
 *     inspired by lonelytwitter lab demo code
 *
 * interface:
 *     MainActivity
 *         which is in charge of everything that happens in the main screen with the list view
 *         and total amount
 *         listview displays each subscription
 *         textview displays the total amount of each subscription
 *         add button on the toolbar where goes to activity_add
 *     activity_add
 *         an activity where the user can create a new subscription and add it to the list
 *         three edittext for the user to enter in name, amount and comment
 *         a textview where the user can select a date
 *         user can press the add button on the toolbar to save the subscription
 *         and go back to MainActivity
 *     activity_edit
 *         same as activity_edit except upon launch the existing data is already in each edittext
 *         the user can change an existing subsciption and save
 *     activity_view
 *         an activity where the user can view the 4 parameters of the subscription
 *         each parameter is displayed on a textview
 *         no edits can be made
 *
 */

public class MainActivity extends AppCompatActivity {
    // initialize variable and their types
    private Toolbar mainToolbar;
    private ListView oldSubList;
    private TextView amountview;
    private ArrayList<Subscription> sublist;

    private ArrayAdapter<Subscription> adapter;
    private static final String FILENAME = "scrub_list.sav";        // save file destination

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // on creation define the listview, textview, and toolbar
        setContentView(R.layout.activity_main);
        // toolbar items defined by the options_menu.xml
        mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        sublist = new ArrayList<>();

        oldSubList = findViewById(R.id.mainlistview);
        amountview = findViewById(R.id.totalview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // on start load sublist from savefile and update the textview with the preexisting amounts
        loadFromFile();
        updateAmount();

        // set up the adapter that puts sublist into the listview and put the stuff up there
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, sublist);
        oldSubList.setAdapter(adapter);
        registerForContextMenu(oldSubList);     // enable contex menu for the listview
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // set up the context menu for long holds on the listview
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // comes here when a long click is done on an item in listview

        // gets the item that was long held
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

        switch (item.getItemId()){
            case R.id.action_view:
                // if option view was selected, pass in the 4 parameters into activity_view and go there
                Intent intentview = new Intent(this, activity_view.class);
                intentview.putExtra("name", sublist.get(info.position).getName());
                intentview.putExtra("amount", String.format(Locale.CANADA,"%.2f",
                        sublist.get(info.position).getAmount()));
                intentview.putExtra("comment", sublist.get(info.position).getComment());
                intentview.putExtra("date", dateFormat.format(sublist.get(info.position).getDate()));

                startActivityForResult(intentview, 1);
                return true;
            case R.id.action_edit:
                // similarly for option edit
                Intent intentedit = new Intent(this, activity_edit.class);
                intentedit.putExtra("name", sublist.get(info.position).getName());
                intentedit.putExtra("amount", String.format(Locale.CANADA, "%.2f",
                        sublist.get(info.position).getAmount()));
                intentedit.putExtra("comment", sublist.get(info.position).getComment());
                intentedit.putExtra("position", String.format("%s", info.position));
                intentedit.putExtra("date", dateFormat.format(sublist.get(info.position).getDate()));

                startActivityForResult(intentedit, 1);
                updateAmount();
                return true;
            case R.id.action_delete:
                // if option delete was selected then delete the selected item from sublist and listview and savefile
                sublist.remove(info.position);
                adapter.notifyDataSetChanged();
                updateAmount();
                saveInFile();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sublist);
                oldSubList.setAdapter(adapter);
            }
        }
    }

    private void updateAmount(){
        // puts the sum of the amount onto the textview
        Float totalamount = 0.0f;
        for (int i = 0; i < sublist.size(); i++) {
            totalamount += sublist.get(i).getAmount();
        }
        amountview.setText(String.format(Locale.CANADA, "$%.2f", totalamount));
    }


    private void loadFromFile() {
        // loadfromfile taken from lonely twitter
        // loads contents from savefile and puts it into sublist
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
        // function taken from lonely twitter
        // saves contents of sublist into savefile
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

    // function that displays the add option on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // sets up the options menu in the toolbar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    // function that does stuff when add is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // upon clicking add in the toolbar go to activity_add
        if(item.getItemId() == R.id.action_add){
            //Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, activity_add.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }
}
