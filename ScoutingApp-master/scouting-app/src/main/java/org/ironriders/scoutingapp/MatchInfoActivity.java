package org.ironriders.scoutingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Iron Riders on 3/11/2016.
 * This class creates and saves the scouting forms
 */
public class MatchInfoActivity extends Activity implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        final Context context = view.getContext();
        //This is the form itself, the EditTexts are where the user inputs information
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.match_info_form, null, false);
        final EditText editTextClass = (EditText) formElementsView.findViewById(R.id.editText2);
        final EditText editTextClassRank = (EditText) formElementsView.findViewById(R.id.editText3);
        final EditText editTextDefenses = (EditText) formElementsView.findViewById(R.id.editText4);
        final EditText editTextAutonomous = (EditText) formElementsView.findViewById(R.id.editText5);
        final EditText editTextStartLoc = (EditText) formElementsView.findViewById(R.id.editText6);
        final EditText editTextBreachSpd = (EditText) formElementsView.findViewById(R.id.editText7);
        final EditText editTextHighGls = (EditText) formElementsView.findViewById(R.id.editText8);
        final EditText editTextShootSpd = (EditText) formElementsView.findViewById(R.id.editText9);
        final EditText editTextLowGls = (EditText) formElementsView.findViewById(R.id.editText10);

        //This saves the inputted information and counts how many records have been created.
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Match Scouting")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ScoutObject scoutObject = new ScoutObject();
                                scoutObject.classification = editTextClass.getText().toString();;
                                scoutObject.classRank = editTextClassRank.getText().toString();
                                scoutObject.defenses = editTextDefenses.getText().toString();
                                scoutObject.auto = editTextAutonomous.getText().toString();
                                scoutObject.startLoc = editTextStartLoc.getText().toString();
                                scoutObject.breach = editTextBreachSpd.getText().toString();
                                scoutObject.highGls = editTextHighGls.getText().toString();
                                scoutObject.shoot = editTextShootSpd.getText().toString();
                                scoutObject.lowGls = editTextLowGls.getText().toString();

                                boolean createSuccessful = new TableControllerScout(context).create(scoutObject);

                                //Toast informs the user whether the scouting form was saved.
                                if(createSuccessful){
                                    Toast.makeText(context, "Scouting information was saved.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Unable to save scouting information.", Toast.LENGTH_SHORT).show();
                                }

                                //This counts the new saved scouting form and has TeamActivity display it
                                ((TeamActivity) context).countRecords();
                                ((TeamActivity) context).readRecords();

                                dialog.cancel();
                            }
                        }).show();
    }


}
