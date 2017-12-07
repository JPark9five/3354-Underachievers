package com.example.briantruong.smsapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        getWindow().setBackgroundDrawableResource(R.drawable.white1) ;

        SMSDisplay();

    }

    public void SMSDisplay()
    {

        sentMessages = (ListView)findViewById(R.id.sentMessageList);

        arrayAdapterSent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sentMessagesList);
        sentMessages.setAdapter(arrayAdapterSent);
        registerForContextMenu(sentMessages);
        refreshSentMessages();
    }

    public void refreshSentMessages() {
        ContentResolver contentResolver = getContentResolver();
        Cursor sentInboxCursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);

        int indexBody = sentInboxCursor.getColumnIndex("body");
        int indexAddress = sentInboxCursor.getColumnIndex("address");
        int indexTime = sentInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !sentInboxCursor.moveToFirst()) return;
        arrayAdapterSent.clear();
        do {
            //Steps to convert parsed timestamp from milliseconds to normal timestamp format
            String timeInMilliseconds = sentInboxCursor.getString(indexTime);
            Long timestamp = Long.parseLong(timeInMilliseconds);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            Date finalDate = calendar.getTime();
            String finalTimestamp = finalDate.toString();

            String str = "Sent to: " + sentInboxCursor.getString(indexAddress) +
                    "\n" + sentInboxCursor.getString(indexBody) + "\n\nat " + finalTimestamp;
            arrayAdapterSent.insert(str, 0);
        } while (sentInboxCursor.moveToNext());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo )
    {
        super.onCreateContextMenu (menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_file,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo obj = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {

            case R.id.delete:
                sentMessagesList.remove(obj.position);
                arrayAdapterSent.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }


}

