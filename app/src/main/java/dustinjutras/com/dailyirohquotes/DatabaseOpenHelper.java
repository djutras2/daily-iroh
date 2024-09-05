package dustinjutras.com.dailyirohquotes;


/**
 * Updated by Dustin Jutras on 11/2/2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static dustinjutras.com.dailyirohquotes.MainActivity.AANG;
import static dustinjutras.com.dailyirohquotes.MainActivity.AZULA;
import static dustinjutras.com.dailyirohquotes.MainActivity.IROH;
import static dustinjutras.com.dailyirohquotes.MainActivity.KATARA;
import static dustinjutras.com.dailyirohquotes.MainActivity.SOKKA;
import static dustinjutras.com.dailyirohquotes.MainActivity.SOUND;
import static dustinjutras.com.dailyirohquotes.MainActivity.TOPH;
import static dustinjutras.com.dailyirohquotes.MainActivity.VIBRATE;
import static dustinjutras.com.dailyirohquotes.MainActivity.ZUKO;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    final static String TABLE_NAME = "getIrohQuotes";
    final static String CHARACTER = "character";
    final static String QUOTE = "quote";
    final static String YEAR = "year";
    final static String DAY_OF_YEAR = "day";

    final static String _ID = "_id";
    final private static String CREATE_CMD =
            "CREATE TABLE " + TABLE_NAME +" ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUOTE + " TEXT NOT NULL,"
                    + YEAR + " INTEGER(2016),"
                    + DAY_OF_YEAR + " INTEGER(1));";

    final private static String CREATE_TABLE_2 =
            "CREATE TABLE " + TABLE_NAME +" ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CHARACTER + " TEXT NOT NULL,"
                    + QUOTE + " TEXT NOT NULL,"
                    + YEAR + " INTEGER(2016),"
                    + DAY_OF_YEAR + " INTEGER(1));";

    final private static String NAME = "workouts_db";
    final private static Integer VERSION = 1;
    final private static Integer VERSION2 = 2; // added other characters
    final private Context context;

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION2);
        this.context = context;
    }

    // version 1
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_CMD);
//
//        for(String s : Quotes.getIrohQuotes()){
//
//            int day = (int) (Math.random()*365);
//            ContentValues cv = new ContentValues(1);
//            cv.put(QUOTE, s);
//            cv.put(YEAR, 2016);
//            cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly
//
//            Log.d("creating Database:", "quote - " + s + ", day - " + day);
//
//            db.insert(TABLE_NAME, null, cv);
//        }
//    }

        @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_2);

            for(String s : Quotes.getIrohQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, IROH);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }

            for(String s : Quotes.getAangQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, AANG);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }

            for(String s : Quotes.getKataraQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, KATARA);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }

            for(String s : Quotes.getTophQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, TOPH);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }

            for(String s : Quotes.getSokkaQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, SOKKA);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }

            for(String s : Quotes.getZukoQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, ZUKO);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }

            for(String s : Quotes.getAzulaQuotes()){

                int day = (int) (Math.random()*365);
                ContentValues cv = new ContentValues(1);
                cv.put(CHARACTER, AZULA);
                cv.put(QUOTE, s);
                cv.put(YEAR, 2016);
                cv.put(DAY_OF_YEAR, day); // get day 1-365 randomly

                //Log.d("creating Database:", "quote - " + s + ", day - " + day);

                db.insert(TABLE_NAME, null, cv);

            }
            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SOKKA, true);
            editor.putBoolean(IROH, true);
            editor.apply();
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        //Log.d(TAG, "onUpgrade: UPGRADING DATABASE");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        switch (oldVersion){
            case 1:
                onUpgradeNotification();
                // move old data to new
        }
    }

    void deleteDatabase ( ) {
        context.deleteDatabase(NAME);
    }


    /**
     * Helper function that parses a given table into a string
     * and returns it for easy printing. The string consists of
     * the table name and then each row is iterated through with
     * column_name: value pairs printed out.
     *
     * @param db the database to get the table from
     * @param tableName the the name of the table to parse
     * @return the table tableName as a string
     */
    public String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        db.close();

        return tableString;
    }

    private void onUpgradeNotification(){
        String message = context.getString(R.string.database_upgrade_message);

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
                    new NotificationChannel("update", "Update Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            mChannel.setDescription("A notification with your daily Iroh quote.");
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);

            //set notification
            Notification.Builder mBuilder =
                    new Notification.Builder(context, "update")
                            .setContentTitle("A Note from Daily Iroh:")
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
                    .setContentTitle("Uncle Iroh Wisdom:")
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

         //vibrate
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

    }
}