package com.example.ccx0.p4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Created by CCx0 on 0015.2016.7.15.
 */
public class Data {
    private static String url="http://192.168.1.151:8080/HttpS/";
    public static String urlservics=url+"Service?";
    public static String urlimages=url+"Images/";
    public static String urltext=url+"Text/1.txt";

    public static int login=0;
    public static String username="";
    public static int size=0;
    public static String shopcartinfor[]=new String[100];
    public static int count[]=new int[100];

    public boolean bool(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("share", activity.MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            return true;
        } else
        {
            return false;
        }
    }
}
