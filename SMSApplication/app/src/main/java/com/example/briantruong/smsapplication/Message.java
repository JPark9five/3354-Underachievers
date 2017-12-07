package com.example.briantruong.smsapplication;
/**
 * Message activity that serves as the main activity. The app starts here and resides here unless
 * user wants to access sent messages list.
 */

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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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

    /**
     * Creation of a broadcast receiver. This will allow the activity to display received messages
     */
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //display the message in the list view
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

        //Aesthetic background implementation
        getWindow().setBackgroundDrawableResource(R.drawable.white1) ;

        SMSDisplay();

        //intent to filter for SMS message received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        SendButton();

        //Assigning a button to go to sent messages list on click
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

    /**
     * SMSDisplay will set up an ArrayAdapter, and if permission to read SMS is granted, will call
     * refreshSmsInbox to fill the ListView with existing messages. Else it will call getPermissionToReadSMS
     * to retrieve permissions to read SMS on the device.
     */
    public void SMSDisplay()
    {
        messages = (ListView)findViewById(R.id.messages);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);

        //Allows for messages to be deleted when user clicks and holds a message
        registerForContextMenu(messages);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            refreshSmsInbox();
        }

    }



    //Will double check if permissions are not granted, then prompt the user to give permissions
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

    //Function to send messages
    public void SendButton()
    {
        btnSend = (Button) findViewById(R.id.btnSend);
        tvMessage = (EditText) findViewById(R.id.tvMessage);
        tvNumber = (EditText) findViewById(R.id.tvNumber);

        //Force disable the send button until permission to send SMS is confirmed, then re-enable once confirmed
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

                //Require the user to have a number and message. User can only enter numbers for number and anything for message.
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

    /**
     * This function, as named, will simply update the Inbox with any new messages
     *
     * @param smsMessage a message to add into the ListView
     */
    public void updateInbox(final String smsMessage) {
        arrayAdapter.add(smsMessage);
        arrayAdapter.notifyDataSetChanged();
    }


    /**
     *
     * @param permission
     * @return true if permission for any permission request returns PERMISSION_GRANTED
     */
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

    /**
     * When opening the activity, this function will parse messages stored in the phone's local database files
     * and update the ListView to include any existing received messages via a URI parse.
     */
    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int indexTime = smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            //Steps to convert parsed timestamp from milliseconds to normal timestamp format using refactored function
            String timeInMilliseconds = smsInboxCursor.getString(indexTime);
            String finalTimestamp = milisecondsToNormalFormat(timeInMilliseconds);

            //Addition of a formatted message into the ListView
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n\nat " + finalTimestamp;
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    /**
     *
     * @param milliseconds a string representing milliseconds
     * @return a string that represents the full date form of the milliseconds passed in
     */
    public String milisecondsToNormalFormat(String milliseconds){
        //Steps to convert parsed timestamp from milliseconds to normal timestamp format
        Long timestamp = Long.parseLong(milliseconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date finalDate = calendar.getTime();

        return finalDate.toString();
    }


    /**
     *
     * @param theNumber a string of the message destination address the user wants to send to
     * @param myMsg a string of the message body that the user wants to send
     */
    protected void sendMsg(String theNumber, String myMsg)
    {

        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        //Pending intents to notify when a message is sent, mostly used for debugging as sent messages will appear in sent messages when sent successfully
        PendingIntent sendPI = PendingIntent.getBroadcast(this,0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sendPI, deliveredPI);
    }

    //Delete Method
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo )
    {
        super.onCreateContextMenu (menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_file,menu);
    }

    /**
     * Implementation of the click and hold to delete functionality in the ListView
     *
     * @param item the message a user clicks and holds
     * @return the deleted item, but doesn't ever get used
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo obj = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {

            case R.id.delete:
                smsMessagesList.remove(obj.position);
                arrayAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
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