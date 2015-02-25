package edu.dartmouth.cs.myrunsjf;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyRunsFragments extends DialogFragment {

    public static final String TAG = "MyRunsFragment.java";
    public static final int DIALOG_ID_ERROR = -1;
    public static final int DIALOG_ID_DATE = 0;
    public static final int DIALOG_ID_TIME = 1;
    public static final int DIALOG_ID_DURATION = 2;
    public static final int DIALOG_ID_DISTANCE = 3;
    public static final int DIALOG_ID_CALORIES = 4;
    public static final int DIALOG_ID_HEARTRATE = 5;
    public static final int DIALOG_ID_COMMENT = 6;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        int currentPosition = getArguments().getInt(ManualInputActivity.INDEX);
        final Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        final EditText et = new EditText(parent);
        switch(currentPosition) {
            case DIALOG_ID_DATE:
                DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Log.d(TAG, "onDateSet()");
                        Calendar cal = new GregorianCalendar();
                        cal.set(year, month, day);
                    }
                };

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
            case DIALOG_ID_TIME:
                TimePickerDialog.OnTimeSetListener onTimeSet = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        Log.d(TAG, "onTimeSet()");
                        Calendar cal = new GregorianCalendar();
                        cal.set(hour, minute);
                        ((ManualInputActivity)getActivity()).getEntry().setmDateTime(cal);
                    }
                };
                // Use the current date as the default date in the picker
                final Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                boolean twentyfour = false;
                // Create a new instance of DatePickerDialog and return it
                return new TimePickerDialog(getActivity(), onTimeSet, hour, minute, twentyfour);
            case DIALOG_ID_DURATION:
                Log.d(TAG, "duration fragment");
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle(R.string.ui_duration_title);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ManualInputActivity)getActivity()).getEntry().setmDuration(Integer.parseInt(et.getText().toString()));
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
            case DIALOG_ID_DISTANCE:
                Log.d(TAG, "distance fragment");
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle(R.string.ui_distance_title);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ManualInputActivity)getActivity()).getEntry().setmDistance(Integer.parseInt(et.getText().toString()));
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
            case DIALOG_ID_CALORIES:
                Log.d(TAG, "calories fragment");
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle(R.string.ui_calories_title);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ManualInputActivity)getActivity()).getEntry().setmCalorie(Integer.parseInt(et.getText().toString()));
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
            case DIALOG_ID_HEARTRATE:
                Log.d(TAG, "heartrate fragment");
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle(R.string.ui_heartrate_title);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ManualInputActivity)getActivity()).getEntry().setmHeartRate(Integer.parseInt(et.getText().toString()));
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
            case DIALOG_ID_COMMENT:
                Log.d(TAG, "comment fragment");
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setHint(R.string.hint_comment);
                builder.setTitle(R.string.ui_comment_title);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ManualInputActivity)getActivity()).getEntry().setmComment(et.getText().toString());
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();

        }
        return null;
    }
}