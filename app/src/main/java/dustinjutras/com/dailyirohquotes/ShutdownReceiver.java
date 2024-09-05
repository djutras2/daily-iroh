package dustinjutras.com.dailyirohquotes;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;

/**
 * Updated by Dustin Jutras on 10/1/2017.
 */

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MyBroadcastReceiver.class);

            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            boolean b = sharedPref.getBoolean("Notifications", false);
            if (b) {    // if notifications are on save when it shutdown
                Calendar calendar = Calendar.getInstance();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("ShutdownHour", (calendar.get(Calendar.HOUR_OF_DAY)));
                editor.putInt("ShutdownMinute", (calendar.get(Calendar.MINUTE)));
                editor.commit();
            }
        }
    }
}
