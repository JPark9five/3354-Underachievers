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
 * Created by Alex on 11/15/2017.
 */

public class Contacts extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        OptionsButton();
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
}
