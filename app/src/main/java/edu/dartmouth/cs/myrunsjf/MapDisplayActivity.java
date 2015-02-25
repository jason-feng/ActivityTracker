package edu.dartmouth.cs.myrunsjf;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapDisplayActivity extends FragmentActivity implements ServiceConnection {

    public static final String TAG = "MapDisplayActivity.java";
    private static final int MENU_ID_DELETE = 0;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private TrackingService trackingService;
    private SensorService sensorService;
    private ServiceConnection mTrackingConnection;
    private ServiceConnection mSensorConnection;
    private ExerciseEntry entry;
    private Long entry_id;
    private ExerciseEntryDataSource db;
    boolean mTrackingIsBound;
    boolean mSensingIsBound;

    private Marker mStartMarker;
    private Marker mFinishMarker;
    private Polyline polyline;

    private TrackingService.TrackingServiceBinder trackingServiceBinder;
    private SensorService.SensorServiceBinder sensorServiceBinder;
    private Intent trackingIntent;
    private Intent sensorIntent;

    private Button save_button;
    private Button cancel_button;

    private TextView activity_type;
    private TextView avg_speed;
    private TextView cur_speed;
    private TextView climb;
    private TextView calorie;
    private TextView distance;
    private int mode;
    private int activity;
    private int input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        setUpMapIfNeeded();

        db = new ExerciseEntryDataSource(this);
        db.open();

        mTrackingConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(TAG, "Tracking: onServiceConnected");
                trackingServiceBinder = (TrackingService.TrackingServiceBinder)binder;
                trackingService = trackingServiceBinder.getService();
                entry = trackingServiceBinder.getExerciseEntry();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "Tracking: onServiceDisconnected");
                trackingService = null;
            }
        };

        mSensorConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentname, IBinder ibinder) {
                Log.d(TAG, "Sensing: onServiceConnected");
                sensorServiceBinder = (SensorService.SensorServiceBinder)ibinder;
                sensorService = sensorServiceBinder.getService();
                activity = (int) sensorServiceBinder.getLabel();
                Log.d(TAG, "New Activity Type: " + activity);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentname) {
                Log.d(TAG, "Sensing: onServiceDisConnected");
                sensorService = null;
            }
        };

        mLocationReceiver = new LocationUpdateReceiver();
        mSensorReceiver = new SensorUpdateReceiver();

        activity_type = (TextView) findViewById(R.id.type);
        avg_speed = (TextView) findViewById(R.id.avg_speed);
        cur_speed = (TextView) findViewById(R.id.cur_speed);
        climb = (TextView) findViewById(R.id.climb);
        calorie = (TextView) findViewById(R.id.calorie);
        distance = (TextView) findViewById(R.id.distance);

        Intent intent = getIntent();
        mode = intent.getIntExtra(StartFragment.ENTRY_TYPE, -1);
        activity = intent.getIntExtra(StartFragment.ACTIVITY_TYPE, -2);
        input = intent.getIntExtra(StartFragment.INPUT_TYPE, -1);
        save_button = (Button)findViewById(R.id.startSaveButton);
        cancel_button = (Button)findViewById(R.id.startCancelButton);

        switch (mode) {
            case (StartFragment.NEW_ENTRY):
                Log.d(TAG, "NEW_ENTRY");
                if (save_button.getVisibility() == View.GONE) {
                    save_button.setVisibility(View.VISIBLE);
                }
                if (cancel_button.getVisibility() == View.GONE) {
                    cancel_button.setVisibility(View.VISIBLE);
                }
                startTrackingService();
                Log.d(TAG, "StartTracking");
                break;
            case (StartFragment.OLD_ENTRY):
                Log.d(TAG, "OLD_ENTRY");
                save_button.setVisibility(View.GONE);
                cancel_button.setVisibility(View.GONE);
                Bundle bundle = getIntent().getExtras();
                entry_id = bundle.getLong("row_id");
                try {
                    entry = db.fetchEntryByIndex(entry_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case (StartFragment.AUTOMATIC_ENTRY):
                Log.d(TAG, "AUTOMATIC_ENTRY");
                startTrackingService();
                startSensingService();
                Log.d(TAG, "StartSensing");
                break;
        }
    }
    private LocationUpdateReceiver mLocationReceiver;

    public class LocationUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "UpdateTraceReceiver onReceive()");
            drawPolyline();
            updateLocationInformation();
        }
    }

    private SensorUpdateReceiver mSensorReceiver;

    public class SensorUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            activity = (int) intent.getDoubleExtra(SensorService.ACTIVITY_TYPE, -1.0);
            Log.d(TAG, "New Activity type from Sensor: " + activity);
            entry.setmActivityType(activity);
            updateLocationInformation();
        }
    }

    private void drawPolyline() {
        Log.d(TAG, "drawPolyline");
        if (entry == null || entry.getmLocationList() == null || entry.getmLocationList().size() == 0) {
            return;
        }
        ArrayList<LatLng> list = entry.getmLocationList();
        LatLng startLatLng = (LatLng)list.get(0);
        LatLng endLatLng = (LatLng)list.get(list.size() - 1);

        if (mStartMarker == null) {
            mStartMarker = mMap.addMarker(new MarkerOptions().position(startLatLng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN)));
        }

        polyline.setPoints(list);

        if (mFinishMarker == null) {
            mFinishMarker = mMap.addMarker(new MarkerOptions().position(endLatLng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED)));
        }
        else {
            mFinishMarker.setPosition(endLatLng);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endLatLng, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(endLatLng)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void updateLocationInformation() {
        Log.d(TAG, "updateLocationInformation");
        if (entry == null || entry.getmActivityType() == -1) {
            return;
        }
        else {
            Log.d(TAG, Integer.toString(entry.getmActivityType()));

            DecimalFormat decimalformat = new DecimalFormat("#.##");
            activity_type.setText("Type: "+ ExerciseEntry.parseActivity(entry.getmActivityType(), this));
            avg_speed.setText("Avg Speed: " + ExerciseEntry.parseSpeed(entry.getmAvgSpeed(), this));
            cur_speed.setText("Cur Speed: "+ ExerciseEntry.parseSpeed(entry.getmCurSpeed(), this));
            climb.setText("Climb: " + ExerciseEntry.parseDistance(entry.getmClimb(), this));
            calorie.setText("Calories: " + decimalformat.format(entry.getmCalorie()));
            distance.setText("Distance: " + ExerciseEntry.parseDistance(entry.getmDistance(), this));
        }
    }

    private void startTrackingService() {
        Log.d(TAG, "startTrackingService");
        trackingIntent = new Intent(this, TrackingService.class);
        trackingIntent.putExtra(StartFragment.INPUT_TYPE, input);
        if (mode == StartFragment.OLD_ENTRY) {
            trackingIntent.putExtra(StartFragment.ACTIVITY_TYPE, activity);
        }
        startService(trackingIntent);
        bindService(trackingIntent, mTrackingConnection, Context.BIND_AUTO_CREATE);
        mTrackingIsBound = true;
    }

    private void startSensingService() {
        Log.d(TAG, "startSensingService");
        sensorIntent = new Intent(this, SensorService.class);
        startService(sensorIntent);
        bindService(sensorIntent, mSensorConnection, Context.BIND_AUTO_CREATE);
        mSensingIsBound = true;
    }


    public void onStartSaveClicked(View v) {
        Log.d(TAG, "onStartSaveClicked");
        if (trackingService != null) {
            if (mTrackingIsBound) {
                unbindService(mTrackingConnection);
                mTrackingIsBound = false;
            }
            stopService(trackingIntent);
        }
        if (sensorService != null) {
            if (mSensingIsBound) {
                unbindService(mSensorConnection);
                mSensingIsBound = false;
            }
            stopService(sensorIntent);
        }
        if (entry != null) {
            entry.updateDuration();
            long id = db.insertEntry(entry);
            Toast.makeText(getApplicationContext(), "Entry #" + id + " saved.",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void onStartCancelClicked(View v) {
        Log.d(TAG, "onStartCancelClicked");
        if (trackingService != null) {
            if (mTrackingIsBound) {
                unbindService(mTrackingConnection);
                mTrackingIsBound = false;
            }
            stopService(trackingIntent);
        }
        if (sensorService != null) {
            if (mSensingIsBound) {
                unbindService(mSensorConnection);
                mSensingIsBound = false;
            }
            stopService(sensorIntent);
        }
        Toast.makeText(getApplicationContext(), getString(R.string.ui_start_cancel_button),
                Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        db.open();
        setUpMapIfNeeded();
        if (mode == StartFragment.NEW_ENTRY || mode == StartFragment.AUTOMATIC_ENTRY) {
            Log.d(TAG, "onResume start Location Receiver");
            IntentFilter intentfilter = new IntentFilter(LocationUpdateReceiver.class.getName());
            registerReceiver(mLocationReceiver, intentfilter);
        }
        if (mode == StartFragment.AUTOMATIC_ENTRY) {
            Log.d(TAG, "onResume start Sensing Receiver");
            IntentFilter intentfilter = new IntentFilter(SensorUpdateReceiver.class.getName());
            registerReceiver(mSensorReceiver, intentfilter);
        }
        drawPolyline();
        updateLocationInformation();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        db.close();
        if (mode == StartFragment.NEW_ENTRY || mode == StartFragment.AUTOMATIC_ENTRY) {
            Log.d(TAG, "unregisterReceiver()");
            unregisterReceiver(mLocationReceiver);
        }
        if (mode == StartFragment.AUTOMATIC_ENTRY) {
            Log.d(TAG, "unregister Sensor Receiver()");
            unregisterReceiver(mSensorReceiver);
        }
        if (mTrackingIsBound) {
            Log.d(TAG, "Unbind tracking service");
            unbindService(mTrackingConnection);
            mTrackingIsBound = false;
        }
        if (mSensingIsBound) {
            Log.d(TAG, "Unbind sensing service");
            unbindService(mSensorConnection);
            mSensingIsBound = false;
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (mode == StartFragment.OLD_ENTRY) {
            MenuItem item = menu.add(Menu.NONE, MENU_ID_DELETE, MENU_ID_DELETE, "Delete");
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == MENU_ID_DELETE) {
            db.removeEntry(entry_id);
            finish();
            return true;
        }

        finish();
        return false;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                polyline = mMap.addPolyline(new PolylineOptions());
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
