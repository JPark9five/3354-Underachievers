package com.example.briantruong.smsapplication;
/**
 * Options activity meant to allow adding, editing, and deleting of contacts.
 * Discontinued early in the development process.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Alex on 11/15/2017.
 */

public class Options extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        AddButton();
        EditButton();
        DeleteButton();
    }

    //View Add Button
    public void AddButton()
    {
        Button add = (Button)findViewById(R.id.addB);

        add.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), AddContact.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }

    //View Edit Button
    public void EditButton()
    {
        Button edit = (Button)findViewById(R.id.editB);

        edit.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), EditContact.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }

    //View Delete Button
    public void DeleteButton()
    {
        Button delete = (Button)findViewById(R.id.deleteB);

        delete.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), DeleteContact.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }
}
