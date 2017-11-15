package com.example.briantruong.smsapplication;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by briantruong on 11/8/17.
 */

public class Message extends AppCompatActivity {

        EditText etMessage;
        EditText etTelNr;
        int MY_PERMISSION_REQUEST_SEND_SMS = 1;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI, deliveredPI;
        BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);

            etMessage = (EditText) findViewById(R.id.editText2);
            etTelNr = (EditText) findViewById(R.id.editText);

            //Shut out the broadcast system
            sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
            deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        }

        @Override
        protected void onResume() {
            super.onResume();

            smsSentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(Message.this, "SMS sent", Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(Message.this, "Generic Failure!", Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(Message.this, "No Service!", Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(Message.this, "No PDU!", Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(Message.this, "Radio Off", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            smsDeliveredReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(Message.this, "SMS Delivered", Toast.LENGTH_SHORT).show();
                            break;

                        case Activity.RESULT_CANCELED:
                            Toast.makeText(Message.this, "SMS Canceled!", Toast.LENGTH_SHORT).show();
                            break;


                    }

                }
            };

            registerReceiver(smsSentReceiver, new IntentFilter(SENT));
            registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
        }

        @Override
        protected void onPause()
        {
            super.onPause();

            unregisterReceiver(smsDeliveredReceiver);
            unregisterReceiver(smsSentReceiver);
        }

        public void btn_SendSMS_OnClick(View view)
        {
            String message = etMessage.getText().toString();
            String telNr = etTelNr.getText().toString();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SEND_SMS);
            }
            else
            {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(telNr, null, message, sentPI, deliveredPI);
            }
        }
}
