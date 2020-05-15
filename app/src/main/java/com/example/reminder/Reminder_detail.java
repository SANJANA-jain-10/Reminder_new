package com.example.reminder;

public class Reminder_detail {
    String reminder_desc;
    String reminder_date;
    String reminder_time;

    public Reminder_detail(){

    }

    public Reminder_detail(String reminder_desc, String reminder_date, String reminder_time){
        this.reminder_desc = reminder_desc;
        this.reminder_date = reminder_date ;
        this.reminder_time = reminder_time;
    }

}
