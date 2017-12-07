package com.example.briantruong.smsapplication;
/**
 * Contact entity made to hold contact information, but contact functionality was
 * discontinued due to inability to complete contact functionality as a whole
 */

import java.io.Serializable;

/**
 * Created by joshu on 11/15/2017.
 */

public class Contact implements Serializable{
    private String name;
    private String number;

    public Contact(String inputName, String inputNumber){
        this.name = inputName;
        this.number = inputNumber;
    }

    public String getName(){
        return this.name;
    }

    public String getNumber(){
        return this.number;
    }
}
