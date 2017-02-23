package com.handigarde.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.handigarde.todoapp.R;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditText;
    private Spinner spEditPriority;
    private String itemPriority;
    private boolean isPending;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemName = getIntent().getStringExtra("ItemName");
        itemPriority = getIntent().getStringExtra("ItemPriority");
        etEditText = (EditText) findViewById(R.id.etEditText);
        etEditText.setText(itemName);
        spEditPriority = (Spinner) findViewById(R.id.spEditPriority);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities_array, android.R.layout.simple_spinner_item);
        spEditPriority.setAdapter(spinnerAdapter);
        isPending = getIntent().getExtras().getBoolean("isPending");
        position = getIntent().getExtras().getInt("position");
    }

    public void onSave(View view) {
        Intent data = new Intent();

        data.putExtra("ItemName", etEditText.getText().toString());
        data.putExtra("ItemPriority", (String) spEditPriority.getSelectedItem());
        data.putExtra("isPending", isPending);
        data.putExtra("position", position);
        data.putExtra("code", 200);

        setResult(RESULT_OK, data);

        finish();
    }
}
