package edu.dartmouth.cs.myrunsjf;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "TrackingService.java";
    private static final String UPDATE_LOCATION = "UPDATE_LOCATION";
    private final IBinder binder = new TrackingServiceBinder();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private NotificationManager mNotificationManager;
    private boolean mRequestingLocationUpdates;
    private ExerciseEntry entry;
    private Context context;

    private void startNotification()
    {
        Intent intent = new Intent(this, MapDisplayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = (new android.app.Notification.Builder(this))
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.powered_by_google_light).setContentIntent(pendingintent).build();
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(0, notification);
    }


    public void onLocationChanged(Location location) {
        if (entry != null) {
            entry.updateLocation(location);
            Intent intent = new Intent(MapDisplayActivity.LocationUpdateReceiver.class.getName());
            intent.putExtra(UPDATE_LOCATION, true);
            context.sendBroadcast(intent);
        }
    }


    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void onConnectionFailed(ConnectionResult connectionresult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public class TrackingServiceBinder extends Binder {
        final TrackingService trackingService;

        public ExerciseEntry getExerciseEntry() {
            return entry;
        }

        TrackingService getService() {
            return TrackingService.this;
        }

        public TrackingServiceBinder() {
            trackingService = TrackingService.this;
        }
    }
    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "bind");
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "requestingLocationUpdates");
            mRequestingLocationUpdates = true;
            mGoogleApiClient.connect();
            startNotification();
            entry = new ExerciseEntry();
            if (arg0.getExtras().getInt(StartFragment.INPUT_TYPE) == StartFragment.GPS) {
                entry.setmInputType(StartFragment.GPS);
                entry.setmActivityType(arg0.getExtras().getInt(StartFragment.ACTIVITY_TYPE));
            }
            else if (arg0.getExtras().getInt(StartFragment.INPUT_TYPE) == StartFragment.AUTOMATIC) {
                entry.setmInputType(StartFragment.AUTOMATIC);
            }
        }
        return binder;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            mGoogleApiClient.connect();
            startNotification();
            entry = new ExerciseEntry();
            entry.getPrivacyPref(context);
            if (intent.getExtras().getInt(StartFragment.INPUT_TYPE) == StartFragment.GPS) {
                entry.setmInputType(StartFragment.GPS);
                entry.setmActivityType(intent.getExtras().getInt(StartFragment.ACTIVITY_TYPE));
            }
            else if (intent.getExtras().getInt(StartFragment.INPUT_TYPE) == StartFragment.AUTOMATIC) {
                entry.setmInputType(StartFragment.AUTOMATIC);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mRequestingLocationUpdates = false;
        context = this;
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        mNotificationManager.cancelAll();
        super.onDestroy();
    }
}