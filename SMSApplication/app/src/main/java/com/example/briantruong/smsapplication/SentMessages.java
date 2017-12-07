package com.example.briantruong.smsapplication;
/**
 * Sent messages activity to display a list of sent messages in a ListView. Allows for deletion of any
 * message in the list via clicking and holding until prompted if the user wants to delete the selected
 * message.
 */

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
import android.widget.Toast;

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

        //Aesthetic background for view
        getWindow().setBackgroundDrawableResource(R.drawable.white1) ;

        SMSDisplay();

    }

    /**
     * SMSDisplay will set up an ArrayAdapter, and will fill the ListView with existing sent messages.
     * The program flow will never let this be the first activity opened, so all permissions should already
     * be granted at this point of the application.
     */
    public void SMSDisplay()
    {

        sentMessages = (ListView)findViewById(R.id.sentMessageList);

        arrayAdapterSent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sentMessagesList);
        sentMessages.setAdapter(arrayAdapterSent);

        //Allows for messages to be deleted when user clicks and holds a message
        registerForContextMenu(sentMessages);
        refreshSentMessages();
    }

    /**
     * Function will access the phone's local database files and fill the ListView with parsed sent messages.
     * All messages will be formatted with target destination, message body, then full date of when it was sent.
     */
    public void refreshSentMessages() {
        ContentResolver contentResolver = getContentResolver();
        Cursor sentInboxCursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);

        //Retrieval of the message body, target destination, and full date
        int indexBody = sentInboxCursor.getColumnIndex("body");
        int indexAddress = sentInboxCursor.getColumnIndex("address");
        int indexTime = sentInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !sentInboxCursor.moveToFirst()) return;
        arrayAdapterSent.clear();
        do {
            //Steps to convert parsed timestamp from milliseconds to normal timestamp format using refactored function
            String timeInMilliseconds = sentInboxCursor.getString(indexTime);
            String finalTimestamp = milisecondsToNormalFormat(timeInMilliseconds);

            //Formatting of the gathered information to add into the ListView
            String str = "Sent to: " + sentInboxCursor.getString(indexAddress) +
                    "\n" + sentInboxCursor.getString(indexBody) + "\n\nat " + finalTimestamp;
            arrayAdapterSent.insert(str, 0);
        } while (sentInboxCursor.moveToNext());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo )
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
                sentMessagesList.remove(obj.position);
                arrayAdapterSent.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }


}

