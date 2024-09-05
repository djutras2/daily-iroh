package dustinjutras.com.dailyirohquotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static dustinjutras.com.dailyirohquotes.DatabaseOpenHelper.CHARACTER;
import static dustinjutras.com.dailyirohquotes.DatabaseOpenHelper.DAY_OF_YEAR;
import static dustinjutras.com.dailyirohquotes.DatabaseOpenHelper.QUOTE;
import static dustinjutras.com.dailyirohquotes.DatabaseOpenHelper.TABLE_NAME;
import static dustinjutras.com.dailyirohquotes.DatabaseOpenHelper.YEAR;

/**
 * Updated by Dustin Jutras on 9/29/2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView txtQuote;
    SharedPreferences sharedPref;

    public static final String IROH = "iroh";
    public static final String KATARA = "katara";
    public static final String AANG = "aang";
    public static final String TOPH = "toph";
    public static final String ZUKO = "zuko";
    public static final String SOKKA = "sokka";
    public static final String AZULA = "azula";

    String theme;

    public static final String VIBRATE = "vibrate";
    public static final String SOUND = "sound";

    public static  final String SKIP = "skip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("onCreate", "Activity created");

        ImageView btnSettings = (ImageView) findViewById(R.id.btnSettings);

        txtQuote = (TextView) findViewById(R.id.txtQuote);
        sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean b = sharedPref.getBoolean("Notifications", true);   // v1.01 update: automatically set notifications on

        //if (!b) {
        notificationsOn(b);
        //}

        //registerReceiver(broadcastReceiver, new IntentFilter("RETURN_TO_ACTIVITY"));

    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d("onStart", "Activity started");
        //inst = this;
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        //int currentQuote = sharedPref.getInt("CurrentQuote", -2);

        //int newQuote = MainActivity.getTodaysQuote(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), currentQuote);
        //txtQuote.setText(Quotes.getIrohQuotes()[newQuote]); // set in app text

        //TODO: TEMPORARY
        if(!sharedPref.getBoolean(SKIP, false)){
            findViewById(R.id.btnNextQuote).setVisibility(GONE);
        } else {
            findViewById(R.id.btnNextQuote).setVisibility(VISIBLE);
        }

        Quote quote = MainActivity.getTodaysQuote(this); // v1.01 update: different quote getting method

        String quoteString = quote.getQuote();
        String quoteCharacter = quote.getCharacter();

        txtQuote.setText(quoteString); // set in app text

        TextView txtIroh = (TextView)findViewById(R.id.txtIroh);
        if (quoteString.contains(":")) {
            txtIroh.setVisibility(GONE);
            txtQuote.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        } else {
            txtIroh.setText(quoteCharacter + ", ");
            txtIroh.setVisibility(VISIBLE);
            txtQuote.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        }

        setTheme(quoteCharacter.toLowerCase());


        //dateCheck();
    }

    public void notificationsOn(boolean b) {
        //alarm turned off
        if (!b) {
            cancelAlarm(this);
        } else {
        // alarm turned on
                setAlarm(this, sharedPref.getInt("Hour", 17), sharedPref.getInt("Minute", 0));
        }

        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Notifications", b);
        editor.apply();
    }

    public static void setAlarm(Context context, int hour, int minute) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //if (checkBox.isChecked()) {
            //Log.d("setAlarm", "Alarm Set");

            Intent intent = new Intent(context, MyBroadcastReceiver.class);
            intent.putExtra("DayCalled", Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            intent.putExtra("Hour", hour);
            intent.putExtra("Minute", minute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 234324243, intent, 0);
            //alarmManager.cancel(pendingIntent);

            int time = calculateSeconds(hour, minute);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time*1000, pendingIntent);
            //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10 * 1000, pendingIntent); //test
        //} else {
        //    //Log.d("SetAlarm", "Alarm Off");
        //}
    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    static public Quote getTodaysQuote(Context context) { // v2 update: different quote getting method

        //Log.d(TAG, "getTodaysQuote: began");

        Cursor cursor;
        SQLiteDatabase db = null;
        DatabaseOpenHelper dbHelper = null;

        String[] columns = new String[] {"_id", CHARACTER, QUOTE, YEAR, DAY_OF_YEAR};

        dbHelper = new DatabaseOpenHelper(context);

        db = dbHelper.getWritableDatabase();

        cursor = db.query(TABLE_NAME, columns, null, null, null, null, YEAR + ", " + DAY_OF_YEAR);

        // 2016 220
        // 2017 150
        // 2017 200
        // ...

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // check most recent to see if it's today
        cursor.moveToLast();

        String q;
        String c = cursor.getString(cursor.getColumnIndex(CHARACTER)); // character from last quote

        if(today == cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR)) && year == cursor.getInt(cursor.getColumnIndex(YEAR))){
            //Log.d(TAG, "getTodaysQuote: grabbed quote that was already set for today");
            q = cursor.getString(cursor.getColumnIndex(QUOTE));
            //c = cursor.getString(cursor.getColumnIndex(CHARACTER));
            //Log.d("fetching quote", "quote - " + q + " by " + c + ", today - " + cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR)));
        } else {
            //Log.d(TAG, "getTodaysQuote: no quote set for today,  searching...");

            // check least recent
            cursor.moveToFirst();

            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            // if the new character is the same as the old, go try another one. don't want to be tooo repetitive
            // gotta make sure that there's not only one character checked though
            if(moreThanOneCharacterChecked(sharedPref)){
                //Log.d(TAG, "getTodaysQuote: more than one character is checked, so can parse through looking for a different character than was just used");
                while (!sharedPref.getBoolean(cursor.getString(cursor.getColumnIndex(CHARACTER)), false)) { // cursor.getString(cursor.getColumnIndex(CHARACTER)).equals(c) ||

                    //Log.d(TAG, "getTodaysQuote: " + cursor.getString(cursor.getColumnIndex(CHARACTER)) + " saved returned " + sharedPref.getBoolean(c, false) + " so keep looking for another quote");

                    //try the next one
                    cursor.moveToNext();
                }
            } else {
                //Log.d(TAG, "getTodaysQuote: only one character checked so get that one");
                while (!sharedPref.getBoolean(cursor.getString(cursor.getColumnIndex(CHARACTER)), false)) {
                    //try the next one
                    cursor.moveToNext();
                }
            }

            ContentValues cv = new ContentValues(1);
            cv.put(YEAR, year);
            cv.put(DAY_OF_YEAR, today);

            q = cursor.getString(cursor.getColumnIndex(QUOTE));
            c = cursor.getString(cursor.getColumnIndex(CHARACTER));

            //Log.d("fetching quote", "quote - " + q + " by " + c + ", day - " + cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR)));

            Log.d("update attempt",String.valueOf(db.update(
                    TABLE_NAME,
                    cv,
                    QUOTE + " = ?",
                    new String[]{q}
            )));
        }
        cursor.close();
        db.close();
        return new Quote(q, c);
    }

    public void parseQuote(View v){
        Cursor cursor;
        SQLiteDatabase db = null;
        DatabaseOpenHelper dbHelper = null;

        String[] columns = new String[] {"_id", CHARACTER, QUOTE, YEAR, DAY_OF_YEAR};

        dbHelper = new DatabaseOpenHelper(this);

        db = dbHelper.getWritableDatabase();

        cursor = db.query(TABLE_NAME, columns, null, null, null, null, YEAR + ", " + DAY_OF_YEAR);

        // 2016 220
        // 2017 150
        // 2017 200
        // ...

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // check most recent to see if it's today
        cursor.moveToLast();

        String q;
        String c = cursor.getString(cursor.getColumnIndex(CHARACTER)); // character from last quote

        if(today == cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR)) && year == cursor.getInt(cursor.getColumnIndex(YEAR))) {
            // it is today, just checking - should always be true tbh

            ContentValues cv = new ContentValues(1);
            cv.put(YEAR, year);
            cv.put(DAY_OF_YEAR, today-1); // make it look in database like it happened yesterday

            q = cursor.getString(cursor.getColumnIndex(QUOTE));
            c = cursor.getString(cursor.getColumnIndex(CHARACTER));

            //Log.d("editing quote", "quote - " + q + " by " + c + ", now day - " + cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR)));

            Log.d("update attempt",String.valueOf(db.update(
                    TABLE_NAME,
                    cv,
                    QUOTE + " = ?",
                    new String[]{q}
            )));

            boolean allSameDay = true;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(sharedPref.getBoolean(cursor.getString(cursor.getColumnIndex(CHARACTER)), false)){
                    if(!( (today - 1 == cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR)) || today == cursor.getInt(cursor.getColumnIndex(DAY_OF_YEAR))) && year == cursor.getInt(cursor.getColumnIndex(YEAR)))){
                        // date is not yesterday
                        allSameDay = false;
                    }
                }
                    cursor.moveToNext();
            }
            if(allSameDay){
                dbHelper.onUpgrade(db, 2,2);
            }

        }
        cursor.close();
        db.close();

        onStart();
    }

    // Calculate time in seconds until alarm should go off
    public static int calculateSeconds(int hour, int minute) {
        int alarm = (hour * 60 + minute) * 60;
        Calendar calendar = Calendar.getInstance();
        int current = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60;
        int difference = alarm - current - calendar.get(Calendar.SECOND);
        if (difference <= 0) { // happening tomorrow, add 24 hours
            difference += 24 * 60 * 60;
        }
        //Log.d(TAG, "calculateSeconds: seconds until alarm: " + difference);
        return difference;
    }

    // Rate the app in the playstore
    public void rateApp(View v){
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    // Share quote with social media
    public void share(View v){

        Quote quote = MainActivity.getTodaysQuote(this); // v1.01 update: different quote getting method

        String quoteString = quote.getQuote();
        String quoteCharacter = quote.getCharacter();

        String message = "Check out today's Uncle Iroh Wisdom:\n\n" + quoteString + "\n-" + quoteCharacter + ", Avatar the Last Airbender"; // v1.01 update: different quote getting method

        message += "\n\nGet the app here:\n" + Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Daily Iroh");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");
        startActivity(intent);
        startActivity(Intent.createChooser(intent, "Send to"));
    }

    public void moreApps(View v){
        //sendLongToast(getString(R.string.moreAppsClicked));

        // developer page
        //Uri uri = Uri.parse("market://details?id=" + "dustinjutras.com.everybodydrinks");
        Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Eeeeeeeeeeee__");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    public void issaSecret(View v){
        sendLongToast("My cabbages! -Cabbage Guy");
    }

    private void sendLongToast(String s){
        Toast newToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        TextView v = newToast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        newToast.show();
    }

    public void openSettings(View v){
        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(this);
        //Log.d(BILLING_TAG, "openSettings: \n\n" + Arrays.toString(dbHelper.getTableAsString(dbHelper.getReadableDatabase(), "getIrohQuotes").split("\n\n")));

        //for (String s : dbHelper.getTableAsString(dbHelper.getReadableDatabase(), "getIrohQuotes").split("\n\n")){
        //    Log.d(TAG, "openSettings: \n\n" + s);
        //}

        Intent intent = new Intent(this, SettingsActivity.class);
        // could put theme and color settings accordingly
        intent.putExtra("theme", theme);
        startActivity(intent);
    }

    public void setTheme(String s){
        theme = s;

        int color;
        int image;
        switch(s){
            case KATARA:
            case SOKKA:
//            case KORRA:
                color = R.color.colorLightBlue;
                image = R.mipmap.ic_iroh_watercircle;
                break;
            case AANG:
                color = R.color.evenLighterBlue;
                image = R.mipmap.launcher_iroh_bluecircle;

                break;
            case ZUKO:
            case AZULA:
                //Log.d(TAG, "setTheme: ZUKO THEME");
                color = R.color.fireKingdom;
                image = R.mipmap.ic_iroh_firecircle;

                break;
            case IROH:
                //Log.d(TAG, "setTheme: IROH THEME");
            case TOPH:
            default:
                color = R.color.earthKingdom;
                image = R.mipmap.launcher_iroh_greencircle;
                break;
        }

        Window window = this.getWindow();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(color));
        }

        ImageView icon = (ImageView) findViewById(R.id.icon);
        icon.setImageResource(image);
    }

    public static boolean moreThanOneCharacterChecked(SharedPreferences sharedPref) {
        int i = 0;
        String[] characters = {SOKKA, AANG, IROH, TOPH, KATARA, ZUKO, AZULA};
        String[] checked = new String[7];
        for (String c : characters) {
            boolean defaultBool = false;
            if(c.equals(IROH) || c.equals(SOKKA)){
                defaultBool = true;
            }
            if (sharedPref.getBoolean(c, defaultBool)) {
                checked[i] = c;
                i++;
            }
            if( i > 1){
                return true;
            }
        }
        //Log.d(TAG, "moreThanOneCharacterChecked: " + Arrays.toString(checked));
        if(i == 0){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SOKKA, true);
            editor.putBoolean(IROH, true);
            editor.apply();
            return true;
        }
        return i > 1;
    }
}
