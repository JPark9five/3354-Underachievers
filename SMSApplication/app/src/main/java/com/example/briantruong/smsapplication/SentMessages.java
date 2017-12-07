package com.example.briantruong.smsapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Josh on 12/6/2017.
 */

public class SentMessages extends AppCompatActivity {

    ArrayList<String> sentMessagesList = new ArrayList<>();
    ListView sentMessages ;
    ArrayAdapter arrayAdapterSent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentmessages);

        SMSDisplay();

    }

    public void SMSDisplay()
    {

        sentMessages = (ListView)findViewById(R.id.sentMessageList);

        arrayAdapterSent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sentMessagesList);
        sentMessages.setAdapter(arrayAdapterSent);
        refreshSentMessages();
    }

    public void refreshSentMessages() {
        ContentResolver contentResolver = getContentResolver();
        Cursor sentInboxCursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);

        int indexBody = sentInboxCursor.getColumnIndex("body");
        int indexAddress = sentInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !sentInboxCursor.moveToFirst()) return;
        arrayAdapterSent.clear();
        do {
            String str = "Sent to: " + sentInboxCursor.getString(indexAddress) +
                    "\n" + sentInboxCursor.getString(indexBody) + "\n";
            arrayAdapterSent.add(str);
        } while (sentInboxCursor.moveToNext());
    }
}
