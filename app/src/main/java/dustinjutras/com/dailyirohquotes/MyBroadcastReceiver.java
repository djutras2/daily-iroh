package dustinjutras.com.dailyirohquotes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static dustinjutras.com.dailyirohquotes.MainActivity.AANG;
import static dustinjutras.com.dailyirohquotes.MainActivity.AZULA;
import static dustinjutras.com.dailyirohquotes.MainActivity.IROH;
import static dustinjutras.com.dailyirohquotes.MainActivity.KATARA;
import static dustinjutras.com.dailyirohquotes.MainActivity.SOKKA;
import static dustinjutras.com.dailyirohquotes.MainActivity.SOUND;
import static dustinjutras.com.dailyirohquotes.MainActivity.TOPH;
import static dustinjutras.com.dailyirohquotes.MainActivity.VIBRATE;
import static dustinjutras.com.dailyirohquotes.MainActivity.ZUKO;

/**
 * Updated by Dustin Jutras on 9/25/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    //handles notification posting

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBroadCastRecive:", "Alarm should be going off now");

        //context.sendBroadcast(new Intent("RETURN_TO_ACTIVITY"));

        intent.getExtras();
        int hour = intent.getIntExtra("Hour", 17);
        int minute = intent.getIntExtra("Minute", 0);
        
        if(hour == 17 && minute == 0){
            Log.d(TAG, "onReceive: this is a problem");
        }

        //v1.01 update

        Quote quote = MainActivity.getTodaysQuote(context); // v1.01 update: different quote getting method

        String quoteString = quote.getQuote();
        String quoteCharacter = quote.getCharacter();

        sendNotification(context, quoteString, quoteCharacter);
        //reset alarm
        MainActivity.setAlarm(context, hour, minute);

    }


    public static void sendNotification(Context context, String quote, String character){

        String message, title;
        if (quote.contains(":")) {
            title = "Today's ATLA Quote:";
            message = quote + "\n-Avatar: the Last Airbender";
        } else {
            title = "Today's " + character + " Quote:";
            message = quote + "\n- " + character + ", Avatar: the Last Airbender";
        }

        // Intent for when notification is clicked on
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //set notification manager
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // set notification channel
            NotificationChannel mChannel =
                    new NotificationChannel("dustinjutras", "Daily Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription("A notification with your daily Iroh quote.");
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.GREEN);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);

            //set notification
            Notification.Builder mBuilder =
                    new Notification.Builder(context, "dustinjutras")
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.icon_iroh_notification)
                            .setContentIntent(notificationPendingIntent)
                            .setAutoCancel(true)
                            //.setSubText("this is the subtext")
                            .setStyle(new Notification.BigTextStyle()
                                    .bigText(message));
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, mBuilder.build());

        } else {

            //set notification
            NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context);
            notificationCompatBuilder
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(notificationPendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.icon_iroh_notification)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message));

            Notification notification = notificationCompatBuilder.build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }

        if(context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getBoolean(SOUND, false)) {
            MediaPlayer mMediaPlayer;
            List<Integer> possibleMedia = new ArrayList<>();
            switch (character.toLowerCase()) {
                case KATARA:
                    possibleMedia.add(R.raw.katara_hey);
                    break;
                case SOKKA:
                    possibleMedia.add(R.raw.sokka_ewwww);
                    possibleMedia.add(R.raw.sokka_holdofyourself);
                    possibleMedia.add(R.raw.sokka_pottybreak);
                    possibleMedia.add(R.raw.sokka_senseofhumor);
                    possibleMedia.add(R.raw.sokka_wooow);
                    break;
//             case KORRA:
                case AANG:
                    possibleMedia.add(R.raw.aang_appayipyip);
                    possibleMedia.add(R.raw.aang_hey);
                    possibleMedia.add(R.raw.aang_penguin);
                   break;
                case ZUKO:
                    possibleMedia.add(R.raw.zuko_idontneedtea);
                    possibleMedia.add(R.raw.zuko_whatturmoil);
                    break;
                case TOPH:
                    possibleMedia.add(R.raw.toph_booksbefore);
                    possibleMedia.add(R.raw.toph_drownnow);
                    possibleMedia.add(R.raw.toph_justlikehim);
                    break;
                case AZULA:
                    possibleMedia.add(R.raw.azula_dominate);
                    possibleMedia.add(R.raw.azula_fearedmemore);
                    possibleMedia.add(R.raw.azula_hahaha);
                    possibleMedia.add(R.raw.azula_peopleperson);
                    break;
                case IROH:
                default:
                    possibleMedia.add(R.raw.iroh_beauty);
                    possibleMedia.add(R.raw.iroh_beggingyou);
                    possibleMedia.add(R.raw.iroh_calmingjasminetea);
                    possibleMedia.add(R.raw.iroh_dragonofthewest);
                    possibleMedia.add(R.raw.iroh_hotleafjuice);
                    possibleMedia.add(R.raw.iroh_noproverb);
                    possibleMedia.add(R.raw.iroh_tenfold);
                    break;
            }

            int media = possibleMedia.get(new Random().nextInt(possibleMedia.size()));

            mMediaPlayer = MediaPlayer.create(context, media);

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.start();
        }

        if(context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getBoolean(VIBRATE, true)) {
            //vibrate
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }
    }
}
