package org.ironriders.scoutingapp;

/**
 * Created by Iron Riders on 3/29/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.*;

/* @author Rebecca Nicacio
 * This class controls all the operations related to the database table
 * Values can be inserted, updated, and deleted using this class.
 */
public class TableControllerScout extends DatabaseHandler {
    public TableControllerScout(Context context) {
        super(context);
    }

    //This inserts the scouting form info into the table
    public boolean create(ScoutObject scoutObject) {

        ContentValues values = new ContentValues();

        values.put("classification", scoutObject.classification);
        values.put("classRank", scoutObject.classRank);
        values.put("defenses", scoutObject.defenses);
        values.put("auto", scoutObject.auto);
        values.put("startLoc", scoutObject.startLoc);
        values.put("breach", scoutObject.breach);
        values.put("highGls", scoutObject.highGls);
        values.put("shoot", scoutObject.shoot);
        values.put("lowGls", scoutObject.lowGls);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("scout", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    //This counts how many scouting forms have been saved into the table
    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM scout";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    //This method reads the info that has been saved in all the forms so it
    //can be displayed later
    public List<ScoutObject> read(){
        List<ScoutObject> recordList = new ArrayList<ScoutObject>();
        String sql = "SELECT * FROM scout ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                ScoutObject scoutObject = new ScoutObject();
                scoutObject.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                scoutObject.classification = cursor.getString(cursor.getColumnIndex("classification"));
                scoutObject.classRank = cursor.getString(cursor.getColumnIndex("classRank"));
                scoutObject.defenses = cursor.getString(cursor.getColumnIndex("defenses"));
                scoutObject.auto = cursor.getString(cursor.getColumnIndex("auto"));
                scoutObject.startLoc = cursor.getString(cursor.getColumnIndex("startLoc"));
                scoutObject.breach = cursor.getString(cursor.getColumnIndex("breach"));
                scoutObject.highGls = cursor.getString(cursor.getColumnIndex("highGls"));
                scoutObject.shoot = cursor.getString(cursor.getColumnIndex("shoot"));
                scoutObject.lowGls = cursor.getString(cursor.getColumnIndex("lowGls"));

                recordList.add(scoutObject);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordList;
    }

    //This reads the info saved in a single form so it can be edited by the user
    public ScoutObject readSingleForm(int scoutId){
        ScoutObject scoutObject = null;
        String sql = "SELECT * FROM scout WHERE id = " + scoutId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            scoutObject = new ScoutObject();
            scoutObject.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            scoutObject.classification = cursor.getString(cursor.getColumnIndex("classification"));
            scoutObject.classRank = cursor.getString(cursor.getColumnIndex("classRank"));
            scoutObject.defenses = cursor.getString(cursor.getColumnIndex("defenses"));
            scoutObject.auto = cursor.getString(cursor.getColumnIndex("auto"));
            scoutObject.startLoc = cursor.getString(cursor.getColumnIndex("startLoc"));
            scoutObject.breach = cursor.getString(cursor.getColumnIndex("breach"));
            scoutObject.highGls = cursor.getString(cursor.getColumnIndex("highGls"));
            scoutObject.shoot = cursor.getString(cursor.getColumnIndex("shoot"));
            scoutObject.lowGls = cursor.getString(cursor.getColumnIndex("lowGls"));
        }

        cursor.close();
        db.close();
        return scoutObject;
    }

    //This allows the user to edit the information in a single form and re-save it
    //to the table
    public boolean update(ScoutObject scoutObject) {

        ContentValues values = new ContentValues();

        values.put("classification", scoutObject.classification);
        values.put("classRank", scoutObject.classRank);
        values.put("defenses", scoutObject.defenses);
        values.put("auto", scoutObject.auto);
        values.put("startLoc", scoutObject.startLoc);
        values.put("breach", scoutObject.breach);
        values.put("highGls", scoutObject.highGls);
        values.put("shoot", scoutObject.shoot);
        values.put("lowGls", scoutObject.lowGls);

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(scoutObject.id) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("scout", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    //This allows the user to delete a scouting form from the database
    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("scout", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;
    }
}
