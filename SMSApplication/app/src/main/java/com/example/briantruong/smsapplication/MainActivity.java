package com.example.briantruong.smsapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    final int sendSMS_PERMISSION_CODE = 111;
    final int receiveSMS_PERMISSION_CODE = 222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermissions(Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    receiveSMS_PERMISSION_CODE);
        }
        if(!checkPermissions(Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    sendSMS_PERMISSION_CODE);
        }

        MessageConnection();
        ViewContacts();
        ViewConversations();
    }

    public boolean checkPermissions(String permission){
        int permissionReturnValue = ContextCompat.checkSelfPermission(this, permission);
        return (permissionReturnValue == PackageManager.PERMISSION_GRANTED);
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
