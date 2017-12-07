package com.example.briantruong.smsapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int sendSMS_PERMISSION_CODE = 111;
    final int receiveSMS_PERMISSION_CODE = 222;
    final int readSMS_PERMISSION_CODE = 333;

    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //display the message in the textview
            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("message"));
            inTxt.setTextColor(Color.BLACK);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (!checkPermissions(Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    receiveSMS_PERMISSION_CODE);
        }
        if (!checkPermissions(Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    readSMS_PERMISSION_CODE);
        }
        if (!checkPermissions(Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    sendSMS_PERMISSION_CODE);
        }

        //intent to filter for SMS message received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        MessageConnection();
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
    public void ViewConversations() {
        Button Conv = (Button) findViewById(R.id.conversations);

        Conv.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SentMessages.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }

    @Override
    protected void onResume()
    {
        //register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        //unregistered receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}
