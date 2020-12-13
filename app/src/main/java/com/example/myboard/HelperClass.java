package com.example.myboard;

public class HelperClass {
    String message,date_time;
    public HelperClass()
    {

    }
    public HelperClass(CharSequence message,String date_time)
    {
        this.message=message.toString();
        this.date_time=date_time;
    }

    public String getMessage() {
        return message;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
