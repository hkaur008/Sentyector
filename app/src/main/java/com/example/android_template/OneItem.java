package com.example.android_template;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OneItem implements Comparable<OneItem>{
    String date;
    String time;
    String response;
    String timeInMillis;

    OneItem(String time, String response){

        this.timeInMillis = time;
        this.response = response;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormatter =new SimpleDateFormat("hh:mm:ss");

        long milliSeconds= Long.parseLong(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        this.date = formatter.format(calendar.getTime());
        this.time = timeFormatter.format(calendar.getTime());


    }

    @NonNull
    @Override
    public String toString() {
        return "date "+this.date+" time "+this.time+" response "+response+"\n";
    }

    @Override
    public int compareTo(OneItem o) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTimeInMillis(Long.parseLong(this.timeInMillis));
        c2.setTimeInMillis(Long.parseLong(o.timeInMillis));

        return c2.getTime().compareTo(c1.getTime());

    }
}
