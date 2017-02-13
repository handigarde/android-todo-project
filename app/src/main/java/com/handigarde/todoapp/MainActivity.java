package com.handigarde.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final int REQUEST_CODE = 20;
    private ListView lvItems;
    private EditText etEditText;
    private ArrayList<String> todoItems;
    private ArrayList<String> completedItems;
    private ArrayAdapter<String> aToDoAdapter;
    private ArrayAdapter<String> aCompletedAdapter;
    private RadioButton rbToDoItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        rbToDoItems = (RadioButton) findViewById(R.id.rbToDoItems);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (rbToDoItems.isChecked()) {
                    String completedItem = todoItems.remove(position);
                    completedItems.add(completedItem);
                    aToDoAdapter.notifyDataSetChanged();
                    aCompletedAdapter.notifyDataSetChanged();
                    writeItems();
                    return true;
                }
                else {
                    completedItems.remove(position);
                    aCompletedAdapter.notifyDataSetChanged();
                    writeItems();
                    return true;
                }
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                if (rbToDoItems.isChecked()){
                    i.putExtra("ItemName", todoItems.get(position));
                }
                else {
                    i.putExtra("ItemName", completedItems.get(position));
                }
                i.putExtra("position", position);
                i.putExtra("isPending", rbToDoItems.isChecked());
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        aCompletedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completedItems);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        File completedFile = new File(filesDir, "completed.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
            completedItems = new ArrayList<String>(FileUtils.readLines(completedFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        File completedFile = new File(filesDir, "completed.txt");
        try {
            FileUtils.writeLines(todoFile, todoItems);
            FileUtils.writeLines(completedFile, completedItems);
        } catch (IOException e) {

        }
    }



    public void onAddItem(View view) {
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was checked
        switch(view.getId()) {
            case R.id.rbToDoItems:
                if (checked) {
                    lvItems.setAdapter(aToDoAdapter);
                }
                break;
            case R.id.rbCompletedItems:
                if (checked) {
                    lvItems.setAdapter(aCompletedAdapter);
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemName = data.getExtras().getString("ItemName");
            boolean isPending = data.getExtras().getBoolean("isPending");
            int position = data.getExtras().getInt("position");
            if (isPending) {
                todoItems.set(position, itemName);
                aToDoAdapter.notifyDataSetChanged();
            }
            else {
                completedItems.set(position, itemName);
                aCompletedAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
