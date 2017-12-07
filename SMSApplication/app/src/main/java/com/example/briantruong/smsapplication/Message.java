package com.example.briantruong.smsapplication;


import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.graphics.Color;

import java.util.ArrayList;


public class Message extends AppCompatActivity {

    Button btnSend, goSentListView;
    EditText tvMessage;
    EditText tvNumber;
    IntentFilter intentFilter;
    private static Message inst;

    ArrayList<String> smsMessagesList = new ArrayList<>();

    ListView messages ;
    ArrayAdapter arrayAdapter;
    SmsManager smsManager = SmsManager.getDefault();

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //display the message in the textview
            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("message"));
            inTxt.setTextColor(Color.BLACK);

        }
    };


    public static Message instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        SMSDisplay();
        //intent to filter for SMS message received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        SendButton();

        goSentListView = (Button) findViewById(R.id.gotoSent);
        goSentListView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent createact = new Intent(Message.this, SentMessages.class);
                startActivity(createact);
            }
        });

    }

    public void SMSDisplay()
    {
        //Display the Previous SMS

        messages = (ListView)findViewById(R.id.messages);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            refreshSmsInbox();
        }

    }


    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    public void SendButton()
    {
        btnSend = (Button) findViewById(R.id.btnSend);
        tvMessage = (EditText) findViewById(R.id.tvMessage);
        tvNumber = (EditText) findViewById(R.id.tvNumber);

        btnSend.setEnabled(false);
        if(checkSMSPermission(Manifest.permission.SEND_SMS)) {
            btnSend.setEnabled(true);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.SEND_SMS},
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
                    tvMessage.setText("");
                    tvNumber.setText("");
                }
                else{
                    Toast.makeText(Message.this, "Number and/or Message is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateInbox(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }


    protected boolean checkSMSPermission(String permission){
        int checkSMSPermissionValue = ContextCompat.checkSelfPermission(this, permission);
        return (checkSMSPermissionValue == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    btnSend.setEnabled(true);
                }
                return;
            }
        }

        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
//messages.setSelection(arrayAdapter.getCount() - 1);
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