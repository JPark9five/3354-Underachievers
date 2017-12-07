package com.example.briantruong.smsapplication;
/**
 * Add contact activity was intended for adding contacts, but couldn't implement whole
 * contact functionality; therefore, we decided to drop the contacts functionality
 * completely.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/15/2017.
 */

public class AddContact extends AppCompatActivity {

    EditText nameText, numberText;
    Intent sendContactBack = new Intent();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add Contact");

        Bundle bundle = getIntent().getExtras();
        nameText = (EditText) findViewById(R.id.nameInput);
        numberText = (EditText) findViewById(R.id.numberInput);
        nameText.setTextColor(Color.BLACK);
        numberText.setTextColor(Color.BLACK);

        final Button addButton = (Button) findViewById(R.id.finishAdd);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                sendContactBack.putExtra("contact", new Contact(nameText.getText().toString(), numberText.getText().toString()));
                setResult(Activity.RESULT_OK, sendContactBack);
                Toast.makeText(getApplicationContext(),"Added " + nameText.getText().toString() + " to contacts.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addButton.setEnabled(!nameText.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
