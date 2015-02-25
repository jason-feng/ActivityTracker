package edu.dartmouth.cs.myrunsjf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ExerciseEntryDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ExerciseEntryDbHelper dbHelper;

    private static final String TAG = "ExerciseEntryDataSource.java";

    private String[] allColumns = {
            ExerciseEntryDbHelper.ROW_ID,
            ExerciseEntryDbHelper.COLUMN_INPUT_TYPE,
            ExerciseEntryDbHelper.COLUMN_ACTIVITY_TYPE,
            ExerciseEntryDbHelper.COLUMN_DATE_TIME,
            ExerciseEntryDbHelper.COLUMN_DURATION,
            ExerciseEntryDbHelper.COLUMN_DISTANCE,
            ExerciseEntryDbHelper.COLUMN_AVG_PACE,
            ExerciseEntryDbHelper.COLUMN_AVG_SPEED,
            ExerciseEntryDbHelper.COLUMN_CALORIES,
            ExerciseEntryDbHelper.COLUMN_CLIMB,
            ExerciseEntryDbHelper.COLUMN_HEART_RATE,
            ExerciseEntryDbHelper.COLUMN_COMMENT,
            ExerciseEntryDbHelper.COLUMN_PRIVACY,
            ExerciseEntryDbHelper.COLUMN_GPS_DATA};


    public ExerciseEntryDataSource(Context context) {
        dbHelper = new ExerciseEntryDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a item given each column value
    public long insertEntry(ExerciseEntry entry) {
        Log.d(TAG, "insertEntry()");
        Log.d(TAG, "input type: " + entry.getmInputType());
        Log.d(TAG, "activity type of entry: " + entry.getmActivityType());
        Log.d(TAG, "duration of entry: " + Integer.toString(entry.getmDuration()));
        Log.d(TAG, "datetime of entry " + entry.getmDateTime().toString());

        long success;
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");

        values.put(ExerciseEntryDbHelper.COLUMN_INPUT_TYPE, entry.getmInputType());
        values.put(ExerciseEntryDbHelper.COLUMN_ACTIVITY_TYPE, entry.getmActivityType());
        values.put(ExerciseEntryDbHelper.COLUMN_DATE_TIME, dateFormat.format(entry.getmDateTime().getTime()));
        values.put(ExerciseEntryDbHelper.COLUMN_DURATION, entry.getmDuration());
        values.put(ExerciseEntryDbHelper.COLUMN_DISTANCE, entry.getmDistance());
        values.put(ExerciseEntryDbHelper.COLUMN_AVG_PACE, entry.getmAvgPace());
        values.put(ExerciseEntryDbHelper.COLUMN_AVG_SPEED, entry.getmCurSpeed());
        values.put(ExerciseEntryDbHelper.COLUMN_CALORIES, entry.getmCalorie());
        values.put(ExerciseEntryDbHelper.COLUMN_CLIMB, entry.getmClimb());
        values.put(ExerciseEntryDbHelper.COLUMN_HEART_RATE, entry.getmHeartRate());
        values.put(ExerciseEntryDbHelper.COLUMN_COMMENT, entry.getmComment());
        values.put(ExerciseEntryDbHelper.COLUMN_PRIVACY, entry.getPrivacy());
        if (entry.getmLocationList() != null) {
            values.put(ExerciseEntryDbHelper.COLUMN_GPS_DATA, entry.getLocationByteArray());
        }

        success = database.insert(ExerciseEntryDbHelper.DATABASE_TABLE, null, values);
        ExerciseEntryDbHelper.entryNumber++;
        return success;
    }

    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
        Log.d(TAG, "removeEntry()");
        database.delete(ExerciseEntryDbHelper.DATABASE_TABLE,
                ExerciseEntryDbHelper.ROW_ID + "=" + rowIndex, null);
    }

    // Query a specific entry by its index.
    public ExerciseEntry fetchEntryByIndex(long rowId) {
        Log.d(TAG, "fetchEntryByIndex()");
        ExerciseEntry entry;
        String where = ExerciseEntryDbHelper.ROW_ID + " = " + rowId;
        Cursor cursor = database.query(ExerciseEntryDbHelper.DATABASE_TABLE, allColumns,where,
            null, null, null, null);

        if (!cursor.moveToFirst()) {
            Log.e("System.out", "cursor is empty");
        }
        else {
            Log.d(TAG, "moveToPosition row id: " + Long.toString(rowId));
            return cursorToEntry(cursor);
        }
        cursor.close();
        return null;
    }

    // Query the entire table, return all rows
    public ArrayList<ExerciseEntry> fetchEntries() {
        ArrayList<ExerciseEntry> list = new ArrayList<ExerciseEntry>();
        Cursor cursor = database.query(ExerciseEntryDbHelper.DATABASE_TABLE, allColumns, null, null,
                null, null, null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ExerciseEntry entry = cursorToEntry(cursor);
            list.add(entry);
            cursor.moveToNext();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    private ExerciseEntry cursorToEntry(Cursor cursor) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
        ExerciseEntry entry = new ExerciseEntry();
        entry = new ExerciseEntry();
        entry.setId(cursor.getLong(0));
        entry.setmInputType(cursor.getInt(1));
        entry.setmActivityType(cursor.getInt(2));
        try {
            Calendar cal = new GregorianCalendar();
            cal.setTime(dateFormat.parse(cursor.getString(3)));
            entry.setmDateTime(cal);
        }
        catch (ParseException exception) {
            System.out.println("Date format error");
        }
        entry.setmDuration(cursor.getInt(4));
        entry.setmDistance(cursor.getInt(5));
        entry.setmAvgPace(cursor.getDouble(6));
        entry.setmAvgSpeed(cursor.getDouble(7));
        entry.setmCalorie(cursor.getInt(8));
        entry.setmClimb(cursor.getDouble(9));
        entry.setmHeartRate(cursor.getInt(10));
        entry.setmComment(cursor.getString(11));
        entry.setPrivacy(cursor.getInt(12));
        if (entry.getmInputType() != StartFragment.MANUAL_ENTRY) {
            entry.setLocationListFromByteArray(cursor.getBlob(13));
        }
        return entry;
    }

    private void deleteDatabase() {
        database.execSQL("DROP TABLE IF EXISTS " + ExerciseEntryDbHelper.DATABASE_TABLE);
    }
}