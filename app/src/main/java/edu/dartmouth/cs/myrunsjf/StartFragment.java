package edu.dartmouth.cs.myrunsjf;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StartFragment extends Fragment {

    public static final String TAG = "StartFragment";
    public static final String INPUT_TYPE = "edu.dartmouth.myrunsjf.ManualInputActivity.java.INPUT_TYPE";
    public static final String ACTIVITY_TYPE = "edu.dartmouth.myrunsjf.ManualInputActivity.java.ACTIVITY_TYPE";
    public static final String ENTRY_TYPE = "edu.dartmouth.myrunsjf.ManualInputActivity.java.ENTRY_TYPE";
    public static final int MANUAL_ENTRY = 4;
    public static final int GPS = 5;
    public static final int AUTOMATIC = 6;
    public static final int NEW_ENTRY = 7;
    public static final int OLD_ENTRY = 8;
    public static final int AUTOMATIC_ENTRY = 9;

    private Spinner inputSpinner;
    private Spinner activitySpinner;
    private Button startButton;
    private Button syncButton;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console.
     */
    private String SENDER_ID = "177679004587";

    private GoogleCloudMessaging gcm;
    private Context context;
    private String regid;
    private ExerciseEntryDataSource datasource;
    private ArrayList<ExerciseEntry> entries;

    private IntentFilter mMessageIntentFilter;
    private BroadcastReceiver mMessageUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Message Update OnReceive()");
            String msg = intent.getStringExtra("message");
            if (msg != null && msg.equals("update")) {
                Log.d(TAG, "Broadcast Receiver Message: " + msg);
            }
            String deleteID = intent.getStringExtra("deleteID");
            if (deleteID != null) {
                Log.d(TAG, "Broadcast Receiver DeleteID: " + deleteID);
                datasource.removeEntry(Long.parseLong(deleteID));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        mMessageIntentFilter = new IntentFilter();
        mMessageIntentFilter.addAction("GCM_NOTIFY");

        context = getActivity();

        datasource = new ExerciseEntryDataSource(getActivity());
        datasource.open();

        entries = datasource.fetchEntries();
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getActivity());
            regid = getRegistrationId(getActivity());

            if (regid.isEmpty()) {
                Log.d(TAG, "RegisterInBackground");
                registerInBackground();
            }
        }
        registerInBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        inputSpinner = (Spinner)v.findViewById(R.id.inputTypeSpinner);
        ArrayAdapter<CharSequence> inputAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.inputArray,android.R.layout.simple_spinner_item);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSpinner.setAdapter(inputAdapter);

        activitySpinner = (Spinner)v.findViewById(R.id.activityTypeSpinner);
        final ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.activityArray,android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        startButton = (Button) v.findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String input = inputSpinner.getSelectedItem().toString();
                if (input.equals("Manual Entry")) {
                    Intent intent = new Intent(getActivity(), ManualInputActivity.class);
                    intent.putExtra(INPUT_TYPE, MANUAL_ENTRY);
                    intent.putExtra(ACTIVITY_TYPE, activitySpinner.getSelectedItemPosition());
                    startActivity(intent);
                }
                else if (input.equals("GPS")) {
                    Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
                    intent.putExtra(INPUT_TYPE, GPS);
                    intent.putExtra(ACTIVITY_TYPE, activitySpinner.getSelectedItemPosition());
                    intent.putExtra(ENTRY_TYPE, NEW_ENTRY);
                    startActivity(intent);
                }
                else if (input.equals("Automatic")) {
                    Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
                    intent.putExtra(INPUT_TYPE, AUTOMATIC);
                    intent.putExtra(ENTRY_TYPE, AUTOMATIC_ENTRY);
                    startActivity(intent);
                }
            }
        });

        syncButton = (Button)v.findViewById(R.id.syncButton);
        syncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "SyncButton OnClick()");
                entries = datasource.fetchEntries();
                sync();
            }
        });

        return v;
    }

    private void sync() {
        Log.d(TAG, "sync()");
        new AsyncTask<ArrayList<ExerciseEntry>, Void, String>() {
            @Override
            protected String doInBackground(ArrayList<ExerciseEntry>... list) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
                String url = getString(R.string.server_addr) + "/post.do";
                String res = "";
                JSONArray jsonArray = new JSONArray();
                ArrayList<ExerciseEntry> entries= list[0];
                for (int i = 0; i < entries.size(); i++) {
                    ExerciseEntry entry = entries.get(i);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("id", entry.getId());
                        json.put("input_type", entry.getmInputType());
                        json.put("activity_type", entry.getmActivityType());
                        json.put("date", dateFormat.format(entry.getmDateTime().getTime()));
                        json.put("duration", entry.getmDuration());
                        json.put("distance", entry.getmDistance());
                        json.put("avg_pace", entry.getmAvgPace());
                        json.put("avg_speed", entry.getmAvgSpeed());
                        json.put("cur_speed", entry.getmCurSpeed());
                        json.put("calories", entry.getmCalorie());
                        json.put("climb", entry.getmClimb());
                        json.put("heart_rate", entry.getmHeartRate());
                        json.put("comment", entry.getmComment());
                        jsonArray.put(i, json);
                    }
                    catch (JSONException exception) {
                        System.out.println("JSONException");
                    }
                }
                Map<String, String> params = new HashMap<>();
                params.put("data", jsonArray.toString());
                try {
                    res = ServerUtilities.post(url, params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return res;
            }

            @Override
            protected void onPostExecute(String res) {
                Log.d(TAG, "EEAsyncTask onPostExecute()");
            }

        }.execute(entries);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        getActivity().registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        getActivity().unregisterReceiver(mMessageUpdateReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                return false;
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        Log.d(TAG, "getRegistrationId");
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        Log.d(TAG, "Registration ID is: " + registrationId);
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(StartFragment.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        Log.d(TAG, "registerInBackground");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over
                    // HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your
                    // app.
                    // The request to your server should be authenticated if
                    // your app
                    // is using accounts.
                    ServerUtilities.sendRegistrationIdToBackend(context, regid);

                    // For this demo: we don't need to send it because the
                    // device
                    // will send upstream messages to a server that echo back
                    // the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, "gcm register msg: " + msg);
            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context
     *            application's context.
     * @param regId
     *            registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

}