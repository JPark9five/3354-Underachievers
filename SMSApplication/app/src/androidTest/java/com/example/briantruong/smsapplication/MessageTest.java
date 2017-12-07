package com.example.briantruong.smsapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.Menu;
import static org.junit.Assert.*;

/**
 * Created by briantruong on 12/6/17.
 */
public class MessageTest extends ListActivity {


    //Testing add listView method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_sentmessages);

        ListView listView;
        String[] name = {"Bao", "Park", "James", "Alex"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, name);
        getListView().setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
                if(id == R.id.sentMessageList)
                {
                    return true;
                }

                return super.onOptionsItemSelected();
    }
}


