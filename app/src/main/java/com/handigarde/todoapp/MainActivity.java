package com.handigarde.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

    private final int REQUEST_CODE = 20;
    private ListView lvItems;
    private EditText etEditText;
    private ArrayList<Task> todoItems;
    private ArrayList<Task> completedItems;
    private CustomListAdapter aToDoAdapter;
    private CustomListAdapter aCompletedAdapter;
    private RadioButton rbToDoItems;
    private Spinner spPrioritySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        rbToDoItems = (RadioButton) findViewById(R.id.rbToDoItems);
        spPrioritySpinner = (Spinner) findViewById(R.id.spPrioritySpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities_array, android.R.layout.simple_spinner_item);
        spPrioritySpinner.setAdapter(spinnerAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (rbToDoItems.isChecked()) {
                    Task completedItem = todoItems.remove(position);
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
                /*Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                if (rbToDoItems.isChecked()){
                    i.putExtra("ItemName", todoItems.get(position).getTask());
                    i.putExtra("ItemPriority", todoItems.get(position).getPriority());
                }
                else {
                    i.putExtra("ItemName", completedItems.get(position).getTask());
                    i.putExtra("ItemPriority", completedItems.get(position).getPriority());
                }
                i.putExtra("position", position);
                i.putExtra("isPending", rbToDoItems.isChecked());
                startActivityForResult(i, REQUEST_CODE);*/
                showEditDialog(position);
            }
        });
    }

    private void showEditDialog(int position) {
        FragmentManager fm = getSupportFragmentManager();
        String title;
        String priority;
        boolean isPending = rbToDoItems.isChecked();
        if (isPending) {
            title = todoItems.get(position).getTask();
            priority = todoItems.get(position).getPriority();
        }
        else {
            title = completedItems.get(position).getTask();
            priority = completedItems.get(position).getPriority();
        }
        EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment.newInstance(title,
                priority, isPending, position);
        editItemDialogFragment.show(fm, "fragment_edit_item");
    }


    public void populateArrayItems() {
        readItems();
        //aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        //aCompletedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completedItems);
        aToDoAdapter = new CustomListAdapter(this, todoItems);
        aCompletedAdapter = new CustomListAdapter(this, completedItems);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        File completedFile = new File(filesDir, "completed.txt");

        try {
            //todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
            //completedItems = new ArrayList<String>(FileUtils.readLines(completedFile));
            todoItems = new ArrayList<Task>();
            completedItems = new ArrayList<Task>();
            FileReader todoReader = new FileReader(todoFile);
            FileReader completedReader = new FileReader(completedFile);
            BufferedReader todoBufferedReader = new BufferedReader(todoReader);
            BufferedReader completedBufferedReader = new BufferedReader(completedReader);
            String line;
            while ((line = todoBufferedReader.readLine()) != null) {
                if (line.indexOf(',') != -1){
                    List<String> splitLine = Arrays.asList(line.split(","));
                    todoItems.add(new Task(splitLine.get(0), splitLine.get(1)));
                }
            }
            while ((line = completedBufferedReader.readLine()) != null) {
                if (line.indexOf(',') != -1){
                    List<String> splitLine = Arrays.asList(line.split(","));
                    completedItems.add(new Task(splitLine.get(0), splitLine.get(1)));
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        File completedFile = new File(filesDir, "completed.txt");
        try {
            // FileUtils.writeLines(todoFile, todoItems);
            // FileUtils.writeLines(completedFile, completedItems);
            if (todoItems.size() < 1) {
                FileUtils.write(todoFile, "");
            }
            else {
                for (Task item : todoItems) {
                    FileUtils.write(todoFile, item.getTask() + "," + item.getPriority());
                }
            }
            if (completedItems.size() < 1) {
                FileUtils.write(completedFile, "");
            }
            else {
                for (Task item : completedItems) {
                    FileUtils.write(completedFile, item.getTask() + "," + item.getPriority());
                }
            }
        } catch (IOException e) {

        }
    }



    public void onAddItem(View view) {
        String text = etEditText.getText().toString();
        if (!text.equals("")) {
            aToDoAdapter.add(new Task(text, (String) spPrioritySpinner.getSelectedItem()));
            etEditText.setText("");
            writeItems();
        }
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
            String itemPriority = data.getExtras().getString("ItemPriority");
            boolean isPending = data.getExtras().getBoolean("isPending");
            int position = data.getExtras().getInt("position");
            if (isPending) {
                todoItems.set(position, new Task(itemName, itemPriority));
                aToDoAdapter.notifyDataSetChanged();
            }
            else {
                completedItems.set(position, new Task(itemName, itemPriority));
                aCompletedAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFinishEditDialog(String taskName, String taskPriority, boolean isPending, int position) {
        if (isPending) {
            todoItems.set(position, new Task(taskName, taskPriority));
            aToDoAdapter.notifyDataSetChanged();
        }
        else {
            completedItems.set(position, new Task(taskName, taskPriority));
            aCompletedAdapter.notifyDataSetChanged();
        }
    }
}
