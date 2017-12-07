package com.example.briantruong.smsapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.ContextMenu;
import android.view.View;


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
        registerForContextMenu(sentMessages);
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
