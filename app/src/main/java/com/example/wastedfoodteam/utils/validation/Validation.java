package com.example.wastedfoodteam.utils.validation;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.wastedfoodteam.utils.CommonFunction;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Validation {

    public static boolean checkPhone(String string) {
        try {
            return Pattern.compile("^(84|0)([35789])([0-9]{8})$").matcher(string).matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkEmail(String string) {
        try {
            return Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(string).matches();
        }catch (Exception e){
            return false;
        }
    }

    public static boolean checkPassword(String string) {
        try {
            return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,16}$").matcher(string).matches();
        }catch ( Exception e){
            return false;
        }
    }

    public static boolean checkName(String string) {
        try{
        return Pattern.compile("^([0-9a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]{1,50}$)+$").matcher(string).matches();}
        catch (Exception e){
            return false;
        }
    }

    public static boolean checkUsername(String string) {
        try{
            return Pattern.compile("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$").matcher(string).matches();}
        catch (Exception e){
            return false;
        }
    }

    @NotNull
    public static Boolean validateDate(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date strDate = sdf.parse(date);
            return new Date().after(strDate);
        } catch (Exception e) {
            Log.d("Lỗi Date : ", date);
            return false;
        }

    }

    public static Boolean checkCurrentDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = sdf.parse(date);
        return new Date().after(strDate);
    }
}
