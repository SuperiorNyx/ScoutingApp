package org.ironriders.scoutingapp;

/**
 * Created by Iron Riders on 3/29/2016.
 * @author Rebecca Nicacio
 *
 * This class stores the recorded
 * scouting info into their categories
 */
public class ScoutObject {
    int id;
    String classification; //Breacher, shooter, defender, etc.
    String classRank; //Rank how well the bot does the functions of its classification (1-10)
    String defenses; //Which defenses it can breach
    String auto; //What it does in autonomous
    String startLoc; //Where the bot starts on the field
    String breach; //How fast the bot can breach the defenses (1-10)
    String highGls; //How many high goals it scored in the match
    String shoot; //How fast it can shoot (1-10)
    String lowGls; //How many low goals it scored in the match
    public ScoutObject(){

    }

}
