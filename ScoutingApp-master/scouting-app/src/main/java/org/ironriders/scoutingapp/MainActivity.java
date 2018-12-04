package org.ironriders.scoutingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.thebluealliance.api.client.Events;
import com.thebluealliance.api.model.v2.Event;

/* @author Zoe Lawrence, Rebecca Nicacio
 * Scouting App 2016 FRC Challenge: FIRST Stronghold
 *
 * This class is the first screen the user
 * will see when they open the app. It allows
 * the user to see all the events for a given year,
 * search and filter for a specific event, and click
 * on an event to view its teams.
 */

public class MainActivity extends Activity {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<Event> adapter;

    // Search EditText
    EditText inputSearch;

    //Package prefix for Intent
    public final static String EVENT_CODE = "org.ironriders.scoutingapp.CODE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        new FetchEventsTask().execute(2016);

        // Adds Events to listview
        adapter = new ArrayAdapter<Event>(this, R.layout.event_list_item, R.id.event_name);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changes the Text, filter for results
                MainActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        lv.setClickable(true);

        //Allows the user to click on an Event to view its teams
        //Clicking on an Event will lead to HomeScreenActivity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(lv.getContext(), HomeScreenActivity.class);
                Object o  = lv.getItemAtPosition(position);
                Event event = (Event)o;
                String event_code = event.getEventCode();
                intent.putExtra(EVENT_CODE, event_code);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //This fetches all the Events from a single year from the BlueAlliance
    //AsyncTask class allows tasks to be performed in in the background and display the results
    private class FetchEventsTask extends AsyncTask<Integer, Integer, Event[]> {
        protected Event[] doInBackground(Integer... year) {
            Events event = Events.getInstance();
            //year[0] is the most current year
            return event.getAllEvents(year[0]);
        }

        protected void onPostExecute(Event[] result) {
            adapter.addAll(result);
        }
    }

}
