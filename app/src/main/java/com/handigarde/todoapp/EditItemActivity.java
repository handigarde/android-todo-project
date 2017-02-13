package com.handigarde.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditText;
    private boolean isPending;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemName = getIntent().getStringExtra("ItemName");
        etEditText = (EditText) findViewById(R.id.etEditText);
        etEditText.setText(itemName);
        isPending = getIntent().getExtras().getBoolean("isPending");
        position = getIntent().getExtras().getInt("position");
    }

    public void onSave(View view) {
        Intent data = new Intent();

        data.putExtra("ItemName", etEditText.getText().toString());
        data.putExtra("isPending", isPending);
        data.putExtra("position", position);
        data.putExtra("code", 200);

        setResult(RESULT_OK, data);

        finish();
    }
}
