package com.example.briantruong.smsapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public Button Cre;

    //Create SMS button
    public void MessageConnection()
    {
        Cre = (Button)findViewById(R.id.create);

        Cre.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                Intent createact = new Intent( MainActivity.this, Message.class);
                startActivity(createact);
               // finish();
            }
            });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessageConnection();
    }


    //Create a sms button


}
