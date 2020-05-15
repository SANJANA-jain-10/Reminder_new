package com.example.reminder;



public class item {

    String date;
    String month;
    String day;
    String desc;
    String time;

    public item()
    {

    }

    public item(String r_date, String r_month, String r_day, String r_desc, String r_time)
    {
        this.date = r_date;
        this.day = r_day;
        this.desc = r_desc;
        this.month = r_month;
        this.time = r_time;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

