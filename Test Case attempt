package com.example.briantruong.smsapplication;

import org.junit.Test;
import org.junit.Before;
import android.widget.EditText;
import static org.junit.Assert.*;

/**
 * Created by jande on 12/6/2017.
 */

public class Test1 extends AddContact{

    public EditText startName=(EditText) findViewById(R.id.nameInput);
    @Before
    public void setUp() throws Exception{//set up places joe in the Name text
        //super.setUp();

        //EditText startName=(EditText) findViewById(R.id.nameInput);

        startName.setText("Joe");

    }
    @Test
    public void testAddContact(){

        AddContact test= new AddContact();
        //EditText startName=(EditText) findViewById(R.id.nameInput); that erro

        //test.startName;
        assertNotNull(test.getEditName());
// assertNotNull and assertNull
    }
    public EditText getEditName(){
        return this.startName;
    }


}
