package com.example.eventapp.Model;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

//import com.republika.gerai.Model.CartItem;


public class Utilities {

    private static final String ABBR_HOUR = " WIB";
    private static final String ABBR_MINUTE = " menit lalu";

    public static String getRelative(long timeMillis) {
        Log.i("TANGGAL BERAPA", String.valueOf(timeMillis));
        long span = Math.max(System.currentTimeMillis() - timeMillis, 0);
       /*if (span >= DateUtils.YEAR_IN_MILLIS) {
           return (span / DateUtils.YEAR_IN_MILLIS) + ABBR_YEAR;
       }
       if (span >= DateUtils.WEEK_IN_MILLIS) {
           return (span / DateUtils.WEEK_IN_MILLIS) + ABBR_WEEK;
       }
       */
        Locale indo = new Locale("id","ID");
        Date df = new Date(timeMillis);
        return new SimpleDateFormat("EEEE, dd MMMM, HH:mm",indo).format(df);
/*
      if (span >= DateUtils.DAY_IN_MILLIS) {
          return new SimpleDateFormat("EEEE, dd MMMM, HH:mm",indo).format(df);
           //return (span / DateUtils.DAY_IN_MILLIS) + ABBR_DAY;
       }
       if (span >= DateUtils.HOUR_IN_MILLIS) {
          return new SimpleDateFormat("HH:mm",indo).format(df) + ABBR_HOUR ;
          // return (span / DateUtils.HOUR_IN_MILLIS) + ABBR_HOUR;
       }
       return (span / DateUtils.MINUTE_IN_MILLIS) + ABBR_MINUTE ;*/
    }


    /**
     * Screen Size Utility goes here
     */
    @SuppressLint("NewApi")
    public static int[] getHeightWidth(Activity activity) {

        int[] heightWidth = new int[2];
        int height, width;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }else{
            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;
        }
        heightWidth[0] = height;
        heightWidth[1] = width;
        return heightWidth;
    }

    public static boolean isEmailValid(String email) {
        //TODO change for your own logic
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean isTitleValid(String title) {
        //TODO change for your own logic
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(title);
        return m.matches();
    }
    public static boolean isContentValid(String content) {
        //TODO change for your own logic
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(content);

        return m.matches();
    }


    public static boolean isPasswordValid(String password) {
        //TODO change for your own logic
        return password.length() > 4;
    }
    public static boolean isConfPasswordValid(String password,String confpassword) {
        //TODO change for your own logic
        return password.equals(confpassword);
    }
    public static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }

    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static  String FCurrency(long f) {
        //  edited here ,change to this
        Locale localeID = new Locale("in", "ID");
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        //DecimalFormat formatter = new DecimalFormat("#.###.##0");
        // Log.i("harganya", String.valueOf( formatRupiah.format(f)));

        DecimalFormat fmt = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        fmt.setGroupingUsed(true);
        fmt.setPositivePrefix("Rp. ");

        return fmt.format(f);
    }
//    public static Long totalPrice(ArrayList<CartItem> p) {
//        double sum = 0;
//        for(int i = 0; i < p.size(); i++) {
//            sum = sum + (p.get(i).getQty() * p.get(i).getPrice());
//        }
//        return (long) sum;
//    }
}