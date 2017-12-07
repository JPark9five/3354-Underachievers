package com.example.briantruong.smsapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ReceiveSMSActivity extends BroadcastReceiver
{
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                String format = intentExtras.getString("format");
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                String timestamp = Long.toString(smsMessage.getTimestampMillis());
                String finalTimeStamp = milliToRegularTime(timestamp);

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
                smsMessageStr += "\nat " + finalTimeStamp;
            }

            Message inst = Message.instance();
            inst.updateInbox(smsMessageStr);
        }
    }

    public String milliToRegularTime(String milli){
        //Steps to convert parsed timestamp from milliseconds to normal timestamp format
        Long timestamp = Long.parseLong(milli);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date finalDate = calendar.getTime();
        String finalTimestamp = finalDate.toString();

        return finalTimestamp;
    }
}
