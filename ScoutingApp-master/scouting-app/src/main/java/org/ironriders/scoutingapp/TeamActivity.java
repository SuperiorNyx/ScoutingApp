package org.ironriders.scoutingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebluealliance.api.client.Teams;
import com.thebluealliance.api.model.v2.Team;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

/* @author Rebecca Nicacio
 * This class allows the user to view each singular Team.
 * It comes with a basic set of information: nickname, number, and years active,
 * as well as a text box to add basic additional info (such as pit scouting).
 * This class also allows users to begin to create scouting forms, as well as
 * counts/displays any saved forms.
 */
public class TeamActivity extends AppCompatActivity {
    private TextView tv;
    private EditText et;
    private String teamFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        //Toolbar doesn't work
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv = (TextView) findViewById(R.id.textView);
        et = (EditText) findViewById(R.id.editText);
        final Button b = (Button) findViewById(R.id.button);


        Intent intent = getIntent();
        new FetchTeamTask().execute(intent.getStringExtra(HomeScreenActivity.KEY_CODE_OF_POWER));

        //This allows the user to edit and save in the Additional Info EditText.
        //The Additional Info EditText is a txt file, not saved in the database
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String string = et.getText().toString();
                OutputStreamWriter outputStream;

                try {
                    outputStream = new OutputStreamWriter(openFileOutput(teamFile, Context.MODE_PRIVATE));
                    outputStream.write(string);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button buttonCreateMatchForm = (Button) findViewById(R.id.button2);
        buttonCreateMatchForm.setOnClickListener(new MatchInfoActivity());
        countRecords();
        readRecords();
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

    //This reads the Add. Info txt file and displays it when the user opens that Team
    private String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Cannot read file: " + e.toString());
        }

        return ret;
    }

    //This uses the count() method in TableControllerScout to
    //display how many forms have been saved.
    public void countRecords() {
        int recordCount = new TableControllerScout(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " scouting forms recorded");
    }

    /*
     * This uses the read() method in TableControllerScout to read the forms that have
     * been saved in the database table. It then takes the contents of the forms and displays them
     * in TextViews. If the user hovers over the TextViews, they can edit/update/delete that record.
     * The only problem with this is that ALL of the forms are displayed, not just the ones for
     * the single Team.
     */
    public void readRecords(){
        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<ScoutObject> scouts = new TableControllerScout(this).read();

        if(scouts.size() > 0){
            for(ScoutObject obj : scouts){
                int id = obj.id;
                String classification = obj.classification;
                String classRank = obj.classRank;
                String defenses= obj.defenses;
                String auto = obj.auto;
                String startLoc = obj.startLoc;
                String breach = obj.breach;
                String highGls = obj.highGls;
                String shoot = obj.shoot;
                String lowGls = obj.lowGls;

                String contents1 = "Classification: " + classification + "\nClass Rank: " + classRank + "\nDefenses: " + defenses;
                String contents2 = "\nAutonomous: " + auto + "\nStarting Location: " + startLoc + "\nBreach Speed: " + breach;
                String contents3 =  "\nHigh Goals Scored: " + highGls + "\nShooting Speed: " + shoot + "\nLow Goals Scored: " + lowGls + "\n";
                String scoutForm = contents1 + contents2 + contents3;

                TextView textViewScoutItem= new TextView(this);
                textViewScoutItem.setPadding(0, 10, 0, 10);
                textViewScoutItem.setText(scoutForm);
                textViewScoutItem.setTag(Integer.toString(id));
                textViewScoutItem.setOnLongClickListener(new OnLongClickListenerMatchRecord());

                linearLayoutRecords.addView(textViewScoutItem);
            }
        } else {
            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");
            linearLayoutRecords.addView(locationItem);
        }
    }

    /*
     * Fetches the team's information from the Blue Alliance
     * and displays it on the screen. It creates a different
     * text file for each team so the right info is displayed
     */
    private class FetchTeamTask extends AsyncTask<String, Integer, Team> {
        protected Team doInBackground(String... key) {
            Teams team = Teams.getInstance();
            return team.getTeam(key[0]);
        }

        protected void onPostExecute(Team result) {
            String name = result.getNickname();
            int team_num = result.getTeamNumber();
            teamFile = result.getTeamNumber() + ".txt";
            int years = 2016 - result.getRookieYear();
            et.setText(readFromFile(teamFile), TextView.BufferType.EDITABLE);
            tv.setText("Name: " + name + "\nTeam Number: " + team_num + "\nYears Participating: " + years);
        }
    }
}


