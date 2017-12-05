package com.example.briantruong.smsapplication;


import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class Message extends AppCompatActivity {

    Button btnSend;
    EditText tvMessage;
    EditText tvNumber;
    IntentFilter intentFilter;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;



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

        //intent to filter for SMS message received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        btnSend = (Button) findViewById(R.id.btnSend);
        tvMessage = (EditText) findViewById(R.id.tvMessage);
        tvNumber = (EditText) findViewById(R.id.tvNumber);

        btnSend.setEnabled(false);
        if(checkSMSPermission(Manifest.permission.SEND_SMS)) {
            btnSend.setEnabled(true);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        btnSend.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String myMsg = tvMessage.getText().toString();
                String theNumber = tvNumber.getText().toString();
                if(!checkSMSPermission(Manifest.permission.SEND_SMS)){
                    Toast.makeText(Message.this, "Send SMS permission not granted", Toast.LENGTH_SHORT).show();
                }

                if(!myMsg.isEmpty() && !theNumber.isEmpty()){
                    sendMsg (theNumber, myMsg);
                }
                else{
                    Toast.makeText(Message.this, "Number and/or Message is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected boolean checkSMSPermission(String permission){
        int checkSMSPermissionValue = ContextCompat.checkSelfPermission(this, permission);
        return (checkSMSPermissionValue == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    btnSend.setEnabled(true);
                }
                return;
            }
        }
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