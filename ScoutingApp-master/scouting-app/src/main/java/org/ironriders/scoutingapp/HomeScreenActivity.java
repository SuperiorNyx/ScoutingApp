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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.thebluealliance.api.client.Events;
import com.thebluealliance.api.model.v2.Team;

/* @author Zoe Lawrence
 * This class allows the user to search for and view
 * all teams from a single Event and allows them to
 * click on that team to view information
 */

public class HomeScreenActivity extends Activity {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<Team> adapter;

    // Search EditText
    EditText inputSearch;

    //Package prefix for the Intent
    public final static String KEY_CODE_OF_POWER = "org.ironriders.scoutingapp.TEAM_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


    lv = (ListView) findViewById(R.id.listView2);
    inputSearch = (EditText) findViewById(R.id.inputSearch2);
    Intent intent = getIntent();
    new FetchTeamsTask().execute(2016 + intent.getStringExtra(MainActivity.EVENT_CODE));

    // Adds Teams to the ListView
    adapter = new ArrayAdapter<Team>(this, R.layout.event_list_item, R.id.event_name);
    lv.setAdapter(adapter);

    //Search and filter for Teams
    inputSearch.addTextChangedListener(new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            // When user changes the text, filter for results
            HomeScreenActivity.this.adapter.getFilter().filter(cs);
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

        //Allows the user to click on a Team to view its information
        //Clicking on a Team will lead to TeamActivity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(lv.getContext(), TeamActivity.class);
                Object o = lv.getItemAtPosition(position);
                Team team = (Team) o;
                String team_key = team.getKey();
                intent.putExtra(KEY_CODE_OF_POWER, team_key);
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

    //This fetches all the teams from a single event from the BlueAlliance
    private class FetchTeamsTask extends AsyncTask<String, Integer, Team[]> {
        protected Team[] doInBackground(String... key) {
            Events event = Events.getInstance();
        return event.getTeams(key[0]);
        }

        protected void onPostExecute(Team[] result) {
        adapter.addAll(result);
    }
    }

}
