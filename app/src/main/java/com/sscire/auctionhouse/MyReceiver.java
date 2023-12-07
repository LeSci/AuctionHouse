package com.sscire.auctionhouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String watchedAction = "android.intent.action.ACTION_POWER_DISCONNECTED";
        if(action != null && action.equals(watchedAction)) {
            Toast.makeText(context, "Action: " + watchedAction, Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context, "IDK what that was " + action, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * https://csumb.hosted.panopto.com/Panopto/Pages/Embed.aspx?id=c50cbabd-9006-4bf4-b262-ac8501478558
     * In the video, this is done in MainActivity() or any Activity attached to a view (I think).
     * Need to also update the AndroidManifest.xml
     */
//    <receiver android:name=".MyReceiver"
//        android:exported="true">
//            <intent-filter>
//                <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED"></action>
//            </intent-filter>
//    </receiver>
//    MyReceiver r;
//    IntentFilter mIntentFilter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        r = new MyReceiver();
//        mIntentFilter = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
//    }
//    public class MyReceiver extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            String watchedAction = "android.intent.action.ACTION_POWER_DISCONNECTED";
//            if(action != null && action.equals(watchedAction)) {
//                Toast.makeText(context, "Action: " + watchedAction, Toast.LENGTH_SHORT).show();
//            } else{
//                Toast.makeText(context, "IDK what that was " + action, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
