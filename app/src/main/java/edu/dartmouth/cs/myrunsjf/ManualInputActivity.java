package edu.dartmouth.cs.myrunsjf;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;


public class ManualInputActivity extends ListActivity {

    public static final String TAG = "ManualInputActivity.java";
    public static final String INDEX = "edu.dartmouth.cs.myrunsjf.ManualInputActivity.java";
    public int currentPosition = 0;
    private Button save_button;
    private Button cancel_button;
    private ExerciseEntryDataSource db;
    private ExerciseEntry entry;

    public ExerciseEntry getEntry() {
        return entry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.startArray, android.R.layout.simple_list_item_activated_1);
        setListAdapter(adapter);

        save_button = (Button)findViewById(R.id.startSaveButton);
        cancel_button = (Button)findViewById(R.id.startCancelButton);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            currentPosition = savedInstanceState.getInt("curChoice", 0);
        }

        db = new ExerciseEntryDataSource(this);
        db.open();

        Calendar cal = Calendar.getInstance();

        // Set default settings
        entry = new ExerciseEntry();
        entry.getPrivacyPref(this);
        entry.setmInputType(getIntent().getIntExtra(StartFragment.INPUT_TYPE, 0));
        entry.setmActivityType(getIntent().getIntExtra(StartFragment.ACTIVITY_TYPE, -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        db.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        db.close();
        super.onPause();
    }

    public void onStartSaveClicked(View v) {
        Log.d(TAG, "onStartSaveClicked");
//        Database stuff
        long id = db.insertEntry(entry);
        Toast.makeText(getApplicationContext(), "Entry #" + id + " saved.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onStartCancelClicked(View v) {
        Log.d(TAG, "onCancelClicked");
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_start_cancel_button),
                Toast.LENGTH_SHORT).show();
        // Close the activity
        finish();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick()");
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, position);
        MyRunsFragments fragment = new MyRunsFragments();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "dialog");
    }
}
