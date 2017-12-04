package com.example.briantruong.smsapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessageConnection();
        ViewContacts();
        ViewConversations();
    }

    //Create SMS button
    public void MessageConnection() {
        Button Cre = (Button) findViewById(R.id.create);

        Cre.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent createact = new Intent(MainActivity.this, Message.class);
                startActivity(createact);
                // finish();
            }
        });
    }


    //View Contacts button
    public void ViewContacts() {
        Button Cont = (Button) findViewById(R.id.contacts);

        Cont.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Contacts.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }

    //View Contacts button
    public void ViewConversations() {
        Button Conv = (Button) findViewById(R.id.conversations);

        Conv.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Conversations.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }
}
