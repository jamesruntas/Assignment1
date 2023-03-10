/**
 * The TransporterRoom subclass. Whenever the player leaves this room by typing the "go" command, he 
 * or she is randomly transported into one of the other rooms in the game. 
 * 
 * One constructor to create a transporter room. in this case, the transporter will always exit to a random room
 * THE SAME RANDOM ROOM PER GAME. random room will reset in a new game. 
 * 
 * 
 * @author James Runtas
 * @version March 9th 2023
 * * Student Number 101109175
 */

import java.util.ArrayList;
import java.util.Random;
public class TransporterRoom extends Room{

    /*
     * Create a transporter room using standard description and name (i) along with the list of rooms in the game. 
     */
    public TransporterRoom(String description, String i, ArrayList<Room> roomsList) {
        super(description, i);
        exits.put("south", findRandomRoom(roomsList));   // the exit for this room is a random one picked from the rooms list
    }
  
    /**
    * Choose a random room.
    *
    * @return The room we end up in upon leaving this one.
    */
    Room findRandomRoom(ArrayList<Room> roomList){
        // create instance of Random class
        Random rand = new Random();
   
        // Generate random integers in range 1 to amount of rooms
        int rand_int = (rand.nextInt(roomList.size())) + 1;

        if (rand_int > roomList.size()){   //just a quick check to make sure we arent going to a null room. 
            return roomList.get(rand_int -1);
        }

        return roomList.get(rand_int);  //return a random index of the rooms list. 
    }

    
}
