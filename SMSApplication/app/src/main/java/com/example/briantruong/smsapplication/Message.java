package com.example.briantruong.smsapplication;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


//Created by Brian Truong on 11/24/2017

public class Message extends AppCompatActivity {

    Button btnSend;
    EditText tvMessage;
    EditText tvNumber;
    IntentFilter intentFilter;
    Button testButton;



    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //display the message in the textview
            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("message"));

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toast toast = Toast.makeText(getApplicationContext(),"Test 1", Toast.LENGTH_SHORT);
        toast.show();


        //intent to filter for SMS message received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        btnSend = (Button) findViewById(R.id.btnSend);
        tvMessage = (EditText) findViewById(R.id.tvMessage);
        tvNumber = (EditText) findViewById(R.id.tvNumber);
        testButton = (Button) findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent cont = new Intent(Message.this,Contacts.class);
                startActivityForResult(cont, 0);
            }

        });

        //getWindow()
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String myMsg = tvMessage.getText().toString();
                String theNumber = tvNumber.getText().toString();
                sendMsg (theNumber, myMsg);
            }

        });
    }

    protected void sendMsg(String theNumber, String myMsg)
    {

        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sendPI = PendingIntent.getBroadcast(this,0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sendPI, deliveredPI);
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