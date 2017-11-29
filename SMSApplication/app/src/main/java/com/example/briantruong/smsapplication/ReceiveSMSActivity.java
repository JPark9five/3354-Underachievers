package com.example.briantruong.smsapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class ReceiveSMSActivity extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //get message passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages;
        String str = "";


        if(bundle != null)
        {
            Object[] pdus =(Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus != null ? pdus.length : 0];
            for (int i = 0; i < messages.length; i++)
            {
                messages[i] = SmsMessage.createFromPdu((byte[]) (pdus != null ? pdus[i] : null));
                str += messages[i].getOriginatingAddress();
                str += ": ";
                str += messages[i].getMessageBody();
                str += "\n";
            }



            //display the message
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            //send a broadcast intent to update the SMS received in a TextView
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message", str);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
