package org.ironriders.scoutingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Iron Riders on 3/31/2016.
 * @author Rebecca Nicacio
 * When a user holds down on a scouting record, this class
 * enables them to update and save or delete that record
 */
public class OnLongClickListenerMatchRecord implements View.OnLongClickListener {
    Context context;
    String id;

    public boolean onLongClick(View v){
        context = v.getContext();
        id = v.getTag().toString();
        final CharSequence[] items = {"Edit", "Delete"};

        //This deletes the scouting form if the user selects "Delete" while also updating the display/record count
        new AlertDialog.Builder(context).setTitle("Scouting Form")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            editRecord(Integer.parseInt(id));
                        } else if (item == 1) {
                            boolean deleteSuccess = new TableControllerScout(context).delete(Integer.parseInt(id));
                            if (deleteSuccess){
                                Toast.makeText(context, "Scouting form was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete scouting form.", Toast.LENGTH_SHORT).show();
                            }

                            ((TeamActivity) context).countRecords();
                            ((TeamActivity) context).readRecords();

                        }

                        dialog.dismiss();

                    }
                }).show();


        return false;
    }

    //This allows the user to edit and save a scouting form
    public void editRecord(final int scoutId) {
        final TableControllerScout tableControllerScout = new TableControllerScout(context);
        ScoutObject scoutObject = tableControllerScout.readSingleForm(scoutId);

        //The method assigns the editable text boxes from the form to new editable texts boxes
        //allowing the user to save new information
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

        editTextClass.setText(scoutObject.classification);
        editTextClassRank.setText(scoutObject.classRank);
        editTextDefenses.setText(scoutObject.defenses);
        editTextAutonomous.setText(scoutObject.auto);
        editTextStartLoc.setText(scoutObject.startLoc);
        editTextBreachSpd.setText(scoutObject.breach);
        editTextHighGls.setText(scoutObject.highGls);
        editTextShootSpd.setText(scoutObject.shoot);
        editTextLowGls.setText(scoutObject.lowGls);

        //This saves the edited information and updates the form and display
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Form")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ScoutObject scoutObject = new ScoutObject();
                                scoutObject.id = scoutId;
                                scoutObject.classification = editTextClass.getText().toString();;
                                scoutObject.classRank = editTextClassRank.getText().toString();
                                scoutObject.defenses = editTextDefenses.getText().toString();
                                scoutObject.auto = editTextAutonomous.getText().toString();
                                scoutObject.startLoc = editTextStartLoc.getText().toString();
                                scoutObject.breach = editTextBreachSpd.getText().toString();
                                scoutObject.highGls = editTextHighGls.getText().toString();
                                scoutObject.shoot = editTextShootSpd.getText().toString();
                                scoutObject.lowGls = editTextLowGls.getText().toString();

                                boolean updateSuccessful = tableControllerScout.update(scoutObject);

                                if(updateSuccessful){
                                    Toast.makeText(context, "Scouting form was updated.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Unable to update scouting form.", Toast.LENGTH_SHORT).show();
                                }

                                ((TeamActivity) context).countRecords();
                                ((TeamActivity) context).readRecords();

                                dialog.cancel();
                            }

                        }).show();


    }



}
