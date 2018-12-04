package org.ironriders.scoutingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Iron Riders on 3/29/2016.
 * @author Rebecca Nicacio
 * This class handles all the SQLite database connection.
 * Essentially, this is the database where the scouting forms are saved
 * A good resource for an SQLite database tutorial is https://www.codeofaninja.com/2013/02/android-sqlite-tutorial.html
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    //Every time the code in the other classes is changed, the DATABASE_VERSION
    //has to be updated to the next number
    private static final int DATABASE_VERSION = 5;
    protected static final String DATABASE_NAME = "ScoutingDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * This has the database create a table that holds all the information the
     * user inputs on the scouting forms, and categorizes it. The table becomes
     * the main part of the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE scout " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " + "classification, " +
                "classRank, " + "defenses, " + "auto, " +
                "startLoc, " + "breach, " + "highGls, " +
                "shoot, " + "lowGls)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS scout";
        db.execSQL(sql);

        onCreate(db);
    }

}
