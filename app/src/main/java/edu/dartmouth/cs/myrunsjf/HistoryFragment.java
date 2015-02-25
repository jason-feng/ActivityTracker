package edu.dartmouth.cs.myrunsjf;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends ListFragment {

    public final static String TAG = "HistoryFragment.java";
    public final static String POSITION = "edu.dartmouth.myrunsjf.HistoryFragment.POSITION";

    private ExerciseEntryDataSource datasource;
    private ActivityEntriesAdapter adapter;
    private Context context;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        datasource = new ExerciseEntryDataSource(getActivity());
        datasource.open();

        ArrayList<ExerciseEntry> values = datasource.fetchEntries();

        adapter = new ActivityEntriesAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, values);
        setListAdapter(adapter);

    }

    public class ActivityEntriesAdapter extends ArrayAdapter<ExerciseEntry> {
        Context context;
        int resources;
        List<ExerciseEntry> objects;

        public ActivityEntriesAdapter(Context context, int resources, List<ExerciseEntry> objects) {
            super(context, resources, objects);
            this.resources = resources;
            this.context = context;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.entry_list_layout, null);
            }

            ExerciseEntry entry = objects.get(position);
            if (entry != null) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");

                TextView upper = (TextView) view.findViewById(R.id.upper_text);
                TextView lower = (TextView) view.findViewById(R.id.lower_text);

                if (upper != null){
                    upper.setText(
                            ExerciseEntry.parseActivity(entry.getmActivityType(), getActivity()) +
                                    ", " + dateFormat.format(entry.getmDateTime().getTime()));
                }
                if (lower != null){
                    lower.setText(ExerciseEntry.parseDistance(entry.getmDistance(), getActivity())
                            + " " + ExerciseEntry.parseDuration(entry.getmDuration(),
                            getActivity()));
                }
            }

            return view;
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick()");
        ExerciseEntry entry = adapter.getItem(position);

        Log.d(TAG, "Entry id: " + Long.toString(entry.getId()));

        if (entry.getmInputType() == StartFragment.MANUAL_ENTRY) {
            Intent intent = new Intent(getActivity(), DisplayEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("_id", entry.getId());
            bundle.putString("activity_type", ExerciseEntry.parseActivity(entry.getmActivityType(),
                    getActivity()));
            bundle.putString("date_time", ExerciseEntry.parseTime(entry.getmDateTime(),
                    getActivity()));
            bundle.putString("duration", ExerciseEntry.parseDuration(entry.getmDuration(),
                    getActivity()));
            bundle.putString("distance", ExerciseEntry.parseDistance(entry.getmDistance(),
                    getActivity()));
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (entry.getmInputType() == StartFragment.GPS || entry.getmInputType() == StartFragment.AUTOMATIC) {
            Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("row_id", entry.getId());
            bundle.putString("activity_type", ExerciseEntry.parseActivity(entry.getmActivityType(),
                    getActivity()));
            bundle.putString("avg_speed", ExerciseEntry.parseSpeed(entry.getmAvgSpeed(),
                    getActivity()));
            bundle.putString("cur_speed", ExerciseEntry.parseSpeed(entry.getmCurSpeed(),
                    getActivity()));
            bundle.putString("climb", ExerciseEntry.parseDistance(entry.getmClimb(),
                    getActivity()));
            bundle.putString("calorie", "Calorie: " + entry.getmCalorie());
            bundle.putString("distance", ExerciseEntry.parseDistance(entry.getmDistance(),
                    getActivity()));
            intent.putExtras(bundle);
            intent.putExtra(StartFragment.ENTRY_TYPE, StartFragment.OLD_ENTRY);
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        updateView();
    }

    public void updateView() {
        Log.d(TAG, "update adapter");
        if (adapter != null) {
            adapter.clear();
            ArrayList<ExerciseEntry> values = datasource.fetchEntries();
            adapter.addAll(values);
            adapter.notifyDataSetChanged();
        }
    }


    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if (resultCode == 0) {
            int position = data.getIntExtra(POSITION, 0);
            adapter.remove(adapter.getItem(position));
        }
    }
}