package dustinjutras.com.dailyirohquotes;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Updated by Dustin Jutras on 10/1/2017.
 */

//v3 update
public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MyBroadcastReceiver.class);

            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            int hour = sharedPref.getInt("Hour", 12);
            int minute = sharedPref.getInt("Minute", 0);
            boolean b = sharedPref.getBoolean("Notifications", false);

            int shutdownHour = sharedPref.getInt("ShutdownHour", 12);
            int shutdownMinute = sharedPref.getInt("ShutdownMinute", 0);


            String s = ", hour: " + hour + ", minute: " + minute + ", notifications: " + b;
            String shutdown = "shutdown at hour: " + shutdownHour + ", minute: " + shutdownMinute;
            Log.d("OnBootReceiver",s);
            Log.d("OnBootReceiver",shutdown);

            //Toast.makeText(context, s, Toast.LENGTH_LONG).show(); // test

            Calendar calendar = Calendar.getInstance();
           // int current = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE))


            if(!b){return;}

//            if (Build.VERSION.SDK_INT >= 26) {
//                Log.d("OnBootReceiver","Sending for alarm to be set");
//                MainActivity.setAlarm(context, hour, minute);
//            }
//            else {
//                context.startService(serviceIntent);
//            }
                if(calendar.get(Calendar.HOUR_OF_DAY) >= hour
                        && calendar.get(Calendar.MINUTE) >= minute
                        && hour >= shutdownHour
                        && minute >= shutdownMinute
                        ){ // set for when phone was dead
                    Log.d("OnBootReceiver","Alarm set for when phone was dead.\nSending for alarm to be set now");
                    MainActivity.setAlarm(context, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 1);
            } else { // time not passed yet
                    Log.d("OnBootReceiver","Sending for alarm to be set");
                    MainActivity.setAlarm(context, hour, minute);
            }
        }
    }
}
