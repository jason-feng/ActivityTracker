package edu.dartmouth.cs.myrunsjf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayEntryActivity extends Activity {

    public static final String TAG = "DisplayEntryActivity.java";
    private static final int MENU_ID_DELETE = 0;
    private ExerciseEntryDataSource datasource;
    private TextView activity_type;
    private TextView date_time;
    private TextView distance;
    private TextView duration;
    private long entry_id;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        datasource = new ExerciseEntryDataSource(this);
        datasource.open();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        entry_id = bundle.getLong("_id");

        activity_type = (TextView)findViewById(R.id.activity_type_text_view);
        date_time = (TextView)findViewById(R.id.date_time_text_view);
        distance = (TextView)findViewById(R.id.distance_text_view);
        duration = (TextView)findViewById(R.id.duration_text_view);

        activity_type.setText(bundle.getString("activity_type"));
        date_time.setText(bundle.getString("date_time"));
        distance.setText(bundle.getString("distance"));
        duration.setText(bundle.getString("duration"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(Menu.NONE, MENU_ID_DELETE, MENU_ID_DELETE, "Delete");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == MENU_ID_DELETE) {
            datasource.removeEntry(entry_id);
            finish();
            return true;
        }

        finish();
        return false;
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
