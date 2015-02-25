package edu.dartmouth.cs.myrunsjf;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class SensorService extends Service implements SensorEventListener {

    public static final String TAG = "SensorService";

    public static final String CLASS_LABEL_KEY = "label";
    public static final String CLASS_LABEL_STANDING = "standing";
    public static final String CLASS_LABEL_WALKING = "walking";
    public static final String CLASS_LABEL_RUNNING = "running";
    public static final int SERVICE_TASK_TYPE_COLLECT = 0;
    public static final int SERVICE_TASK_TYPE_CLASSIFY = 1;
    public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";

    private final IBinder binder = new SensorServiceBinder();
    private boolean mRequestingSensorUpdates;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double[] mAccBuffer;
    private int index;
    private Double label;
    private Context context;


    /** Called when the activity is first created. */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        mRequestingSensorUpdates = false;
        context = this;
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (!mRequestingSensorUpdates) {
            mRequestingSensorUpdates = true;
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sensorManager
                    .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            mAccBuffer = new double[ACCELEROMETER_BUFFER_CAPACITY];
            label = -1.0;
            index = 0;
        }
        return START_STICKY;
    }

    public class SensorServiceBinder extends Binder {
        final SensorService sensorService;

        public double getLabel() {
            return label;
        }

        SensorService getService() {
            return SensorService.this;
        }

        public SensorServiceBinder() {
            sensorService = SensorService.this;
        }
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        Log.i("","");
        super.onDestroy();

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            double m = Math.sqrt(x * x + y * y + z * z);

            if (index == 64) {
                try {
                    label = WekaClassifier.classify(createFeatureVector(mAccBuffer).toArray());
                    Log.d(TAG, "New label: " + Double.toString(label));
                    Intent intent = new Intent(MapDisplayActivity.SensorUpdateReceiver.class.getName());
                    intent.putExtra(ACTIVITY_TYPE, label);
                    context.sendBroadcast(intent);
                }
                catch (Exception e){

                }
                index = 0;
                mAccBuffer = new double[ACCELEROMETER_BLOCK_CAPACITY];
            }
            mAccBuffer[index] = m;
            index++;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        if (!mRequestingSensorUpdates) {
            mRequestingSensorUpdates = true;
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            label = -2.0;
        }
        return binder;
    }

    public double max(double[] arr) {
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public ArrayList<Double> createFeatureVector(double[] accBlock) {
        FFT fft = new FFT(accBlock.length);
        ArrayList<Double> featVect = new ArrayList<Double>();
        double[] re = accBlock;
        double[] im = new double[accBlock.length];
        double max = max(accBlock);

        // Compute the re and im:
        // setting values of re and im by reference.
        fft.fft(re, im);

        for (int i = 0; i < re.length; i++) {
            // Compute each coefficient
            double mag = Math.sqrt(re[i] * re[i] + im[i]* im[i]);
            // Adding the computed FFT coefficient to the
            // featVect
            featVect.add(Double.valueOf(mag));
            // Clear the field
            im[i] = .0;
        }

        // Finally, append max after frequency components
        featVect.add(Double.valueOf(max));
        return featVect;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}