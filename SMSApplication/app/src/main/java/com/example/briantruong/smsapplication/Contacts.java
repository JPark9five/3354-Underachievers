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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/15/2017.
 */

public class Contacts extends AppCompatActivity {

    Contact temporaryContact;
    List<Contact> ContactList = new ArrayList<>();
    ListView ContactListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ContactListView = (ListView) findViewById(R.id.contactListView);
        final Intent addContactIntent = new Intent(this, AddContact.class);
        Button addContactButton = (Button) findViewById(R.id.addButton);
        addContactButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivityForResult(addContactIntent, 1);
            }
        });

        setTitle("Contact List");
        OptionsButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                temporaryContact = (Contact) data.getSerializableExtra("contact");
                ContactList.add(temporaryContact);
                fillContactList();
            }
            if(resultCode == Activity.RESULT_CANCELED){
                System.out.println("Major error happened in adding contact");
            }
        }
    }

    //View Options button
    public void OptionsButton()
    {
        Button opt = (Button)findViewById(R.id.optionB);

        opt.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Options.class);
                startActivityForResult(myIntent, 0);
                // finish();
            }
        });
    }


    private class ContactsAdapter extends ArrayAdapter<Contact>{
        public ContactsAdapter(){
            super(Contacts.this, R.layout.contact_item, ContactList);
        }

        public View getView(int position, View view, ViewGroup root){
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.contact_item, root, false);
            }
            Contact curContact = ContactList.get(position);

            TextView name = (TextView) view.findViewById(R.id.singleContactName);
            TextView number = (TextView) view.findViewById(R.id.singleContactNum);

            name.setText(curContact.getName());
            number.setText(curContact.getNumber());

            return view;
        }
    }

    private void fillContactList(){
        ArrayAdapter<Contact> listAdapter = new ContactsAdapter();
        ContactListView.setAdapter(listAdapter);
    }
}
