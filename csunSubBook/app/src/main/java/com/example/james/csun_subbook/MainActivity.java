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

public class MainActivity extends AppCompatActivity {
    Toolbar mainToolbar;
    ListView oldSubList;
    TextView amountview;
    ArrayList<Subscription> sublist;

    private ArrayAdapter<Subscription> adapter;
    private static final String FILENAME = "scrub_list.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        sublist = new ArrayList<>();

        oldSubList = findViewById(R.id.mainlistview);
        amountview = findViewById(R.id.totalview);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();

        updateAmount();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sublist);
        oldSubList.setAdapter(adapter);
        registerForContextMenu(oldSubList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

        switch (item.getItemId()){
            case R.id.action_view:
                Intent intentview = new Intent(this, activity_view.class);
                intentview.putExtra("name", sublist.get(info.position).getName());
                intentview.putExtra("amount", sublist.get(info.position).getAmount().toString());
                intentview.putExtra("comment", sublist.get(info.position).getComment());

                intentview.putExtra("date", dateFormat.format(sublist.get(info.position).getDate()));

                startActivityForResult(intentview, 1);
                return true;
            case R.id.action_edit:
                Intent intentedit = new Intent(this, activity_edit.class);
                intentedit.putExtra("name", sublist.get(info.position).getName());
                intentedit.putExtra("amount", sublist.get(info.position).getAmount().toString());
                intentedit.putExtra("comment", sublist.get(info.position).getComment());
                intentedit.putExtra("position", String.format("%s", info.position));

                intentedit.putExtra("date", dateFormat.format(sublist.get(info.position).getDate()));

                startActivityForResult(intentedit, 1);
                updateAmount();
                return true;
            case R.id.action_delete:
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

    public void updateAmount(){
        Float totalamount = 0.0f;
        for (int i = 0; i < sublist.size(); i++) {
            totalamount += sublist.get(i).getAmount();
        }
        amountview.setText(String.format(Locale.CANADA, "$%.2f", totalamount));
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
        if(item.getItemId() == R.id.action_add){
            //Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, activity_add.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }
}
