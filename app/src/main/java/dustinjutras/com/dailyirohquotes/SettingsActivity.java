package dustinjutras.com.dailyirohquotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import dustinjutras.com.dailyirohquotes.util.IabBroadcastReceiver;
import dustinjutras.com.dailyirohquotes.util.IabHelper;
import dustinjutras.com.dailyirohquotes.util.IabResult;
import dustinjutras.com.dailyirohquotes.util.Inventory;
import dustinjutras.com.dailyirohquotes.util.Purchase;

import static dustinjutras.com.dailyirohquotes.MainActivity.AANG;
import static dustinjutras.com.dailyirohquotes.MainActivity.AZULA;
import static dustinjutras.com.dailyirohquotes.MainActivity.IROH;
import static dustinjutras.com.dailyirohquotes.MainActivity.KATARA;
import static dustinjutras.com.dailyirohquotes.MainActivity.SKIP;
import static dustinjutras.com.dailyirohquotes.MainActivity.SOKKA;
import static dustinjutras.com.dailyirohquotes.MainActivity.SOUND;
import static dustinjutras.com.dailyirohquotes.MainActivity.TOPH;
import static dustinjutras.com.dailyirohquotes.MainActivity.VIBRATE;
import static dustinjutras.com.dailyirohquotes.MainActivity.ZUKO;

public class SettingsActivity extends AppCompatActivity implements  IabBroadcastReceiver.IabBroadcastListener{

    // BILLING STUFF
    public final static String BILLING_TAG = "billing";
    boolean mIsPremium = false;
    static final String SKU_PREMIUM = "premium_upgrade";
    static final int RC_REQUEST = 1001;
    IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;


    private TimePicker timePicker;
    private Switch switchNotifications;

    private Switch switchIroh;
    private Switch switchAang;
    private Switch switchKatara;
    private Switch switchSokka;
    private Switch switchZuko;
    private Switch switchToph;
    private Switch switchAzula;
    private Switch switchVibrate;
    private Switch switchSoundEffect;
    private Switch switchSkipButton;

    SharedPreferences sharedPref;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTheme(getIntent().getStringExtra("theme"));

        context = this;

        timePicker = (TimePicker) findViewById(R.id.tp);
        switchNotifications = (Switch) findViewById(R.id.switchNotifications);

        //Set a TimeChangedListener for TimePicker widget
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                saveTime();
                MainActivity.setAlarm(SettingsActivity.this, getHour(), getMinute());
            }
        });

        switchNotifications.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationsSwitchChange(b);
            }
        });

        sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int hour = sharedPref.getInt("Hour", 12);
        int minute = sharedPref.getInt("Minute", 0);

        setHour(hour);
        setMinute(minute);

        switchIroh = (Switch) findViewById(R.id.switchIroh);
        switchKatara = (Switch) findViewById(R.id.switchKatara);
        switchAang = (Switch) findViewById(R.id.switchAang);
        switchSokka = (Switch) findViewById(R.id.switchSokka);
        switchToph = (Switch) findViewById(R.id.switchToph);
        switchZuko = (Switch) findViewById(R.id.switchZuko);
        switchAzula = (Switch) findViewById(R.id.switchAzula);
        switchSkipButton = (Switch) findViewById(R.id.switchSkipButton);

        switchVibrate = (Switch) findViewById(R.id.switchVibrate);
        switchSoundEffect = (Switch) findViewById(R.id.switchVoiceSoundEffect);

        switchIroh.setChecked(sharedPref.getBoolean(IROH, true));
        switchKatara.setChecked(sharedPref.getBoolean(KATARA, false));
        switchAang.setChecked(sharedPref.getBoolean(AANG, false));
        switchSokka.setChecked(sharedPref.getBoolean(SOKKA, true));
        switchToph.setChecked(sharedPref.getBoolean(TOPH, false));
        switchZuko.setChecked(sharedPref.getBoolean(ZUKO, false));
        switchAzula.setChecked(sharedPref.getBoolean(AZULA, false));

        switchVibrate.setChecked(sharedPref.getBoolean(VIBRATE, true));
        switchSoundEffect.setChecked(sharedPref.getBoolean(SOUND, false));

        switchSkipButton.setChecked(sharedPref.getBoolean(SKIP, false));


        switchIroh.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveCharacter(IROH, b);
            }
        });

        switchAang.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkForPurchase()) {
                    saveCharacter(AANG, b);
                } else {
                    switchAang.setChecked(false);
                }
            }
        });

        switchKatara.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkForPurchase()) {
                    saveCharacter(KATARA, b);
                } else {
                    switchKatara.setChecked(false);
                }
            }
        });
        switchToph.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkForPurchase()) {
                    saveCharacter(TOPH, b);
                } else {
                    switchToph.setChecked(false);
                }
            }
        });
        switchSokka.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveCharacter(SOKKA, b);
            }
        });
        switchZuko.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkForPurchase()) {
                    saveCharacter(ZUKO, b);
                } else {
                    switchZuko.setChecked(false);
                }
            }
        });
        switchAzula.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkForPurchase()) {
                    saveCharacter(AZULA, b);
                } else {
                    switchAzula.setChecked(false);
                }
            }
        });



        switchSoundEffect.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(checkForPurchase()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(SOUND, b);
                    editor.apply();
                } else {
                    switchSoundEffect.setChecked(false);
                }
            }
        });
        switchVibrate.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(VIBRATE, b);
                editor.apply();
            }
        });

        switchSkipButton.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkForPurchase()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(SKIP, b);
                    editor.apply();
                } else {
                    switchSkipButton.setChecked(false);
                }
            }
        });

        // BILLING STUFF

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmVM72ZJRGrO9wFQhdVYNkYkBaU0KZ4jxKQ5IHTfoomZgP+z6iMKszzTZaQHidi4rfdRSAdk2yUmO8b3zrqZIlXZXaid4IuIOkbLyLFxTKnuTRwpz28D26tJFcbMivZDEWrFYSpCB0YaXv8S4nfnBrsu7OY+Jd79Ja7sWg/PUu7VbCKJdLXwpRKXjPp3778sdzV4ozMWi3S4dCQBy1Zf9seFlnbxWg7goeIqxyLzA2pDdEz01dxTGtRvnYz14ryNTfYDmODMzWVe9S90cE3tUCWcEvt53SDfPEEnawEFvFlXQGf04SfKEqYYBGKujp6CuRZfEX8aCJUNUZnWLgbqkhwIDAQAB";

        //Log.d(BILLING_TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        //Log.d(BILLING_TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                //Log.d(BILLING_TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    //Log.d(BILLING_TAG, "Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(SettingsActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                //Log.d(BILLING_TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    //Log.d(BILLING_TAG, "Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    public int getHour(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return timePicker.getHour();
        } else {
            return timePicker.getCurrentHour();
        }
    }

    public int getMinute(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return timePicker.getMinute();
        }
        else{
            return timePicker.getCurrentMinute();
        }
    }

    public void setHour(int hour){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
        } else {
            timePicker.setCurrentHour(hour);
        }
    }

    public void setMinute(int minute){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setMinute(minute);
        }
        else{
            timePicker.setCurrentMinute(minute);
        }
    }

    public void notificationsSwitchChange(boolean b) {
        switchNotifications.setChecked(b);
        timePicker.setEnabled(b);

        //alarm turned off
        if (!b) {
            MainActivity.cancelAlarm(this);
            //Toast.makeText(this, getString(R.string.notificationsOff), Toast.LENGTH_SHORT).show();
        } else {
            // alarm turned on
            saveTime();
            MainActivity.setAlarm(this, getHour(), getMinute());
        }

        SharedPreferences sharedPref = SettingsActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Notifications", b);
        editor.apply();
    }

    public void saveTime() {
        //Display the new time to app interface
        //Log.d("saveTime", "Alarm time set to:\nH:M | " + timePicker.getHour() + ":" + timePicker.getMinute());
        SharedPreferences sharedPref = SettingsActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Hour", getHour());
        editor.putInt("Minute", getMinute());
        editor.apply();
    }

    public void saveCharacter(String s, boolean b){

        // if nothing is checked
        if(        !switchIroh.isChecked()
                && !switchKatara.isChecked()
                && !switchAang.isChecked()
                && !switchSokka.isChecked()
                && !switchToph.isChecked()
                && !switchZuko.isChecked()
                && !switchAzula.isChecked()
                ){

            switchIroh.setChecked(true);
            saveCharacter(IROH, true);
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(s, b);
        editor.apply();
    }

    public void setTheme(String s){
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
                color = R.color.fireKingdom;
                image = R.mipmap.ic_iroh_firecircle;

                break;
            case IROH:
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

    }

    public void premiumContentDialog(){
        //if(notPremium)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.purchase_message).setTitle(R.string.purchase_title);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                tryPurchase();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });

        builder.create().show();
    }

    public boolean checkForPurchase(){
        if(!mIsPremium){
            premiumContentDialog();
            return false;
        } else {
            return true;
        }
        //return true;  //TODO: TEMPORARY
    }

    public void tryPurchase(){
        try {
            String payload = "";

            mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            //Log.d(BILLING_TAG, "Error launching purchase flow. Another async operation in progress.");
            //setWaitScreen(false);
        }
    }

    // MORE BILLING STUFF

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            //Log.d(BILLING_TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                //Log.d(BILLING_TAG, "Failed to query inventory: " + result);
                return;
            }

            //Log.d(BILLING_TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            //Log.d(BILLING_TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        //Log.d(BILLING_TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(BILLING_TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            //Log.d(BILLING_TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            //Log.d(BILLING_TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                //Log.d(BILLING_TAG, "Error purchasing: " + result);
                //setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                //Log.d(BILLING_TAG, "Error purchasing. Authenticity verification failed.");
                //setWaitScreen(false);
                return;
            }

            //Log.d(BILLING_TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                //Log.d(BILLING_TAG, "Purchase is premium upgrade. Congratulating user.");
                //alert("Thank you for upgrading to premium!");
                mIsPremium = true;
                autoTurnOnPremiumFeatures();
                //setWaitScreen(false);
            }
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        //Log.d(BILLING_TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            //Log.d(BILLING_TAG, "Error querying inventory. Another async operation in progress.");
        }
    }

    public void autoTurnOnPremiumFeatures(){
        switchSkipButton.setChecked(true);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SKIP, true);

        editor.putBoolean(IROH, true);
        editor.putBoolean(KATARA, true);
        editor.putBoolean(SOKKA, true);
        editor.putBoolean(SKIP, true);
        editor.putBoolean(TOPH, true);
        editor.putBoolean(ZUKO, true);
        editor.putBoolean(AZULA, true);

        editor.apply();

        switchIroh.setChecked(sharedPref.getBoolean(IROH, true));
        switchKatara.setChecked(sharedPref.getBoolean(KATARA, true));
        switchAang.setChecked(sharedPref.getBoolean(AANG, true));
        switchSokka.setChecked(sharedPref.getBoolean(SOKKA, true));
        switchToph.setChecked(sharedPref.getBoolean(TOPH, true));
        switchZuko.setChecked(sharedPref.getBoolean(ZUKO, true));
        switchAzula.setChecked(sharedPref.getBoolean(AZULA, true));
    }
}
