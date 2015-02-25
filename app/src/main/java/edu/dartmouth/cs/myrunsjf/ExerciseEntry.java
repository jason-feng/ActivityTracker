package edu.dartmouth.cs.myrunsjf;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jasonfeng on 1/27/15.
 */
public class ExerciseEntry {

    public static final int ERROR = -1;
    public static final int STANDING = 0;
    public static final int WALKING = 1;
    public static final int RUNNING = 2;
    public static final int CYCLING = 3;
    public static final int HIKING = 4;
    public static final int DOWNHILL_SKIING = 5;
    public static final int CROSS_COUNTRY_SKIING = 6;
    public static final int SNOWBOARDING = 7;
    public static final int SKATING = 8;
    public static final int SWIMMING = 9;
    public static final int MOUNTAIN_BIKING = 10;
    public static final int WHEELCHAIR = 11;
    public static final int ELLPITICAL = 12;
    public static final int OTHER = 13;
    // Constants for distance/time conversions
    public static final double MILE_TO_KM = 1.609344;
    // Different format to display the information
    public static final String DATE_FORMAT = "HH:mm:ss MMM dd yyyy";
    public static final String DISTANCE_FORMAT = "#.##";
    public static final String MINUTES_FORMAT = "%d mins";
    public static final String SECONDS_FORMAT = "%d secs";

    private Long id;
    private int mInputType;        // Manual, GPS or automatic
    private int mActivityType;     // Running, cycling etc.
    private Calendar mDateTime;    // When does this entry happen
    private int mDuration;         // Exercise duration in seconds
    private double mDistance;      // Distance traveled. Either in meters or feet.
    private double mAvgPace;       // Average pace
    private double mCurSpeed;      // Current speed
    private double mAvgSpeed;      // Average speed
    private int mCalorie;          // Calories burnt
    private double mClimb;         // Climb. Either in meters or feet.
    private int mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private ArrayList<LatLng> mLocationList; // Location list
    private Location mLastLocation; // Location
    private int privacy;

    public ExerciseEntry() {
        this.mInputType = -1;
        this.mActivityType = -1;
        this.mDateTime = Calendar.getInstance();
        this.mDuration = 0;
        this.mDistance = 0.0;
        this.mAvgPace = 0.0;
        this.mAvgSpeed = 0.0;
        this.mCalorie = 0;
        this.mClimb = 0.0;
        this.mHeartRate = 0;
        this.mComment = "";
        this.mLocationList = null;
        this.mLastLocation = null;
        this.privacy = 0;

    }

    public static String parseActivity(int activity_type, Context context) {
        String[] activities =  context.getResources().getStringArray(R.array.activityArray);
        return activities[activity_type];
    }

    public static String parseTime(Calendar calendar, Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(calendar.getTime());
    }

    public static String parseDistance(double distanceInMeters, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String unit = prefs.getString("unit_preference", "Imperial");
        if (unit.equals("Metric")) {
            unit = "Kilometers";
            double distInKM = distanceInMeters / 1000;
            DecimalFormat decimalFormat = new DecimalFormat(DISTANCE_FORMAT);
            return decimalFormat.format(distInKM) + " " + unit;
        }
        else {
            unit = "Miles";
            double distInMiles = distanceInMeters / 1000 * MILE_TO_KM;
            DecimalFormat decimalFormat = new DecimalFormat(DISTANCE_FORMAT);
            return decimalFormat.format(distInMiles) + " " + unit;
        }
    }

    // Convert duration in seconds to minutes.
    public static String parseDuration(int durationInSeconds, Context context) {
        StringBuilder duration = new StringBuilder("");
        int min = durationInSeconds / 60;
        int sec = durationInSeconds % 60;

        if (min > 0) {
            duration.append(min + "mins ");
        }
        duration.append(sec + "secs");

        return duration.toString();
    }

    public static String parseSpeed(double speed, Context context) {
        DecimalFormat decimalFormat = new DecimalFormat(ExerciseEntry.DISTANCE_FORMAT);
        double speedInMPH = speed;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String unit = prefs.getString("unit_preference", "Imperial");
        if (unit.equals("Imperial")) {
            unit = "Miles";
        }
        else {
            unit = "Kilometers";
            double speedInKPH = speed * ExerciseEntry.MILE_TO_KM;
            return decimalFormat.format(speedInKPH) + " " + unit;
        }
        return decimalFormat.format(speedInMPH) + " " + unit;
    }

    public void updateDuration() {
        Calendar currentTime = Calendar.getInstance();
        Calendar startTime = getmDateTime();
        long timeDifference = currentTime.getTimeInMillis() - startTime.getTimeInMillis();
        if (timeDifference < Integer.MIN_VALUE || timeDifference > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (timeDifference+ " cannot be cast to int without changing its value.");
        }
        setmDuration((int)timeDifference/1000);
        setmAvgSpeed(getmDistance() / getmDuration());
    }

    public void updateLocation(Location location) {
        if (getmLocationList() == null) {
            ArrayList<LatLng> list = new ArrayList<LatLng>();
            list.add(new LatLng(location.getLatitude(), location.getLongitude()));
            setmLocationList(list);
        }
        else {
            getmLocationList().add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        if(mLastLocation == null) {
            setmAvgSpeed(0.0);
            setmClimb(0.0);
            setmDistance(0.0);
            setmCalorie(0);
            mLastLocation = location;
        }
        else {
            setmDistance(getmDistance() + (double)Math.abs(location.distanceTo(mLastLocation)));
            setmClimb(getmClimb() + (location.getAltitude() - mLastLocation.getAltitude()));
            setmCalorie((int)(getmDistance()/15));
            setmCurSpeed(location.getSpeed());
            updateDuration();
            mLastLocation = location;
        }
    }

    // Convert Location ArrayList to byte array, to store in SQLite database
    public byte[] getLocationByteArray() {
        int[] intArray = new int[mLocationList.size() * 2];

        for (int i = 0; i < mLocationList.size(); i++) {
            intArray[i * 2] = (int) (mLocationList.get(i).latitude * 1E6);
            intArray[(i * 2) + 1] = (int) (mLocationList.get(i).longitude * 1E6);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length
                * Integer.SIZE);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);

        return byteBuffer.array();
    }


    // Convert byte array to Location ArrayList
    public void setLocationListFromByteArray(byte[] bytePointArray) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytePointArray);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        int[] intArray = new int[bytePointArray.length / Integer.SIZE];
        intBuffer.get(intArray);

        int locationNum = intArray.length / 2;

        mLocationList = new ArrayList<LatLng>();

        for (int i = 0; i < locationNum; i++) {
            LatLng latLng = new LatLng((double) intArray[i * 2] / 1E6F,
                    (double) intArray[i * 2 + 1] / 1E6F);
            mLocationList.add(latLng);
        }
    }

    public void getPrivacyPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("privacy_preference", false) == true) {
            setPrivacy(1);
        }
        else {
            setPrivacy(0);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getmInputType() {
        return mInputType;
    }

    public void setmInputType(int mInputType) {
        this.mInputType = mInputType;
    }

    public int getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(int mActivityType) {
        this.mActivityType = mActivityType;
    }

    public Calendar getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Calendar mDateTime) {
        this.mDateTime = mDateTime;
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public double getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public double getmCurSpeed() {
        return mCurSpeed;
    }

    public void setmCurSpeed(double mCurSpeed) {
        this.mCurSpeed = mCurSpeed;
    }

    public int getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(int mCalorie) {
        this.mCalorie = mCalorie;
    }

    public double getmClimb() {
        return mClimb;
    }

    public void setmClimb(double mClimb) {
        this.mClimb = mClimb;
    }

    public int getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public ArrayList<LatLng> getmLocationList() {
        return mLocationList;
    }

    public void setmLocationList(ArrayList<LatLng> mLocationList) {
        this.mLocationList = mLocationList;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }
}
