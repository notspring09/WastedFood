package com.example.wastedfoodteam.utils.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeCount {
    public String countTimeAgo(long time ){
        long timeDifferent =   Calendar.getInstance().getTimeInMillis() - time ;
        if(timeDifferent > 0){
            long seconds = timeDifferent / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            //String time = days + ":" + hours % 24 + ":" + minutes % 60;
            if(days >= 1){
                return " " + days + " ngày trước";
            }else if (days < 1 && hours >= 1)  {
                return " " + hours + " giờ trước";
            }else if (minutes > 0){
                return " " + minutes + " phút trước";
            }else{
                return " 1 phút trước";
            }
            //return days + " NGÀY " + + hours%24 +" GIỜ"  + minutes%60  + " PHÚT TRƯỚC";
        }else {
            return " 1 phút trước";
        }
    }

    public String countTimeRemain(long time ){
        long timeDifferent = time - Calendar.getInstance().getTime().getTime();
        if(timeDifferent > 0){
            long seconds = timeDifferent / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            //String time = days + ":" + hours % 24 + ":" + minutes % 60;
             if (hours >= 1)  {
                return " Còn " + hours + " giờ " + minutes%60 + " phút";
            }else if (minutes > 0){
                return " Còn " + minutes + " phút";
            }
            //return days + " NGÀY " + + hours%24 +" GIỜ"  + minutes%60  + " PHÚT TRƯỚC";
        }else {
            return "Đã kết thúc";
        }
        return "";
    }

}

