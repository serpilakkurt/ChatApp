package com.example.vschatapp.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDate {

    public static String getDate() {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        Date today = Calendar.getInstance().getTime();

        String reporDate = df.format(today);

        return reporDate;
    }
}
