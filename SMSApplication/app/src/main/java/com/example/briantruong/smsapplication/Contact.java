package com.example.briantruong.smsapplication;

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
