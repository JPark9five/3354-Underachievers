package com.example.briantruong.smsapplication;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by joshu on 12/6/2017.
 */
public class ReceiveSMSActivityTest {

    @Test
    public void milliToRegularTime() throws Exception {
        String input = "1512624922940";
        String expected_output = "Wed Dec 06 2017 23:35:22 CST 2017";
        String actual_output;
        double delta = .1;

        actual_output = milliToRegularTime(input);

        assertEquals(expected_output, actual_output, delta);

    }

    @Test
    public void milisecondsToNormalFormat() throws Exception
    {
        String i = "198235671982";
        String expected = "Sun Oct 15 2017 13:12:47 CST 2017";
        String actual;
        double delta = .1;

        actual = milliToRegularTime(i);

        assertEquals(expected, actual, delta);
    }

}
