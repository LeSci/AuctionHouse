package com.sscire.auctionhouse;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Util {

    public static void toastMaker(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
