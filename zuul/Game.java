/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery, look around, eat, and grab items.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms and items and creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version October 21, 2012
 * 
 * 
 * @author James Runtas
 * @version February 18th 2023
 * * Student Number 101109175
 */


 import java.util.ArrayList;
 import java.util.*;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Stack<Room> previousRooms;  //a stack of rooms previously visited. last in last out 
    private Room previousRoom;    //previous room is a room object
    private Boolean hungry;     //hungry yes or no
    private  ArrayList<Item> backpack; // store items picked up by users


    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();    //line 53 function
        parser = new Parser();
        hungry = true;
        backpack = new ArrayList<Item>();
        previousRooms = new Stack<Room>();
    }

    /**
     * Create all the rooms and link their exits.
     * 6 items will be linked to the rooms in seperate directions from the exits.
     * this was done to give the room more of a video game feel instead of just automatticaly picking up items,
     * they are spread out in different directions of the room and must be picked up manually. 
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office; //5 rooms 
      
        // create the rooms and their identifiers 
        outside = new Room("outside the main entrance of the university", "outside");
        theatre = new Room("in a lecture theatre", "theatre");
        pub = new Room("in the campus pub", "pub");
        lab = new Room("in a computing lab", "lab");
        office = new Room("in the computing admin office", "office");
        
        //create the item objects 
        Item chestArmor = new Item("Level One bronze Armor", 50.0, "Chest Armor");
        Item helmet = new Item("Level One bronze Helmet", 50.0, "Helmet");
        Item sword = new Item("Level One wooden Sword", 50.0, "Sword");
        Item cat = new Item("a black cat, grants lucky perk", 50.0, "Cat");
        Item shield = new Item("Level one wooden shield ", 50.0, "Shield");
        Item bigChungus = new Item("a huge chungus, grants +500 carrying capacity", 500.0, "Big Chungus");

        // initialise room exits and linked to the next room
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        theatre.setExit("west", outside);
        pub.setExit("east", outside);
        lab.setExit("north", outside);
        lab.setExit("east", office);
        office.setExit("west", lab);

        // initialise room items, what item and where they are stashed
        outside.setItem("north", chestArmor);
        theatre.setItem("east", sword);
        pub.setItem("south", helmet);
        lab.setItem("south", cat);
        lab.setItem("west", bigChungus);
        office.setItem("north", shield);


        currentRoom = outside;  // start game outside
        previousRoom = null;    //when game starts, previous room is not a thing
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        System.out.println("Player hunger status: " + Game.this.hungry);
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        
        
        //unknown command by user
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        //command is valid
        String commandWord = command.getCommandWord();


        if (commandWord.equals("help")) {
            printHelp(); 
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            lookAround(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("grab")) {
            grab(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        for (String str : parser.getCommands()) {
            System.out.println(str);  // print the value of the strings returned by parser (parser.commands is just a reference to the strings)
        }
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        //the direction should be the second word of the command
        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);


        // check if there is a exit in the users given direction 
        if (nextRoom == null) {
            System.out.println("There is no door!");
            return;
        }   
        else { //here if there is a valid exit 
            previousRoom = currentRoom;   //update previous room
            currentRoom = nextRoom;       //update current room
            previousRooms.push(previousRoom);    //add a previous room to our stack of rooms visited
            System.out.println(currentRoom.getLongDescription());
        }
    }



    /**
     * Go back to previous room if the previous room isnt null
     * since this doesnt use a stack data structure, when the back function is used twice, 
     * player will return to the room they were just in back and forth. until stackBack is called. 
     * I assume this is bug is part of the assignment since stackBack will solve this issue. 
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            // if there is a second word, we don't know where to go...
            System.out.println("back only requires one input 'back'");
            return;
        }
        if (previousRoom == null) {  //no room visited prior
            System.out.println("No previous room!");
            return;
        }
        else {
            currentRoom = previousRoom;  //update current room is the previous room, this will get us stuck in loop when invoked more than once, which is why stackBack is needed
            System.out.println(currentRoom.getLongDescription());
        }
    }


    /**
     * Go back to the previous room if applicable
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            // if there is a second word, we don't know where to go...
            System.out.println("stackback only requires one input 'stackback'");
            return;
        }
        if (previousRooms.empty()) {
            System.out.println("No previous room!");
            return;
        }
        else {
            previousRoom = previousRooms.peek(); //the new previous room was already pushed to the top of the stack during goRoom
            currentRoom = previousRooms.pop(); //the new current room is the top of the stack because the new current is the previous 
            
            System.out.println(currentRoom.getLongDescription());
        }
    }



    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }


    /**Look around command function, will return the information for the current room. 
    * @param command The command to be processed
    * @return void, only prints
    */
    private void lookAround(Command command) 
    {
        //print room info
        System.out.println(currentRoom.getShortDescription());

        // check directions 
        String[] directions = {"north", "south", "east", "west"};
        for (int i = 0; i < directions.length; i++) {
            //look for exits in all directions 
          if (currentRoom.getExit(directions[i]) != null){
            System.out.println("There is an exit " + directions[i]);
          }
          //look for items in all directions
          if (currentRoom.getItem(directions[i]) != null){
            System.out.println("There is an item " + directions[i]);
          }
        }
    }
    
    /**Player eats something to reset hunger 
    * @param command The command to be processed
    * @return void, only prints
    */
    private void eat(Command command) 
    {
        //change the players hungry value
        Game.this.hungry = false;
        System.out.println("Player hunger status: " + Game.this.hungry);
    }


    /**
     * Try to grab an item in a certain direction. If there is an item, add to backpack and show item info
     * otherwise print an error message. item will not be in same direction as an exit, therefore player always stays in same room
     * 
     * @param command The command to be processed
     */
    private void grab(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go to pick up item...
            System.out.println("Grab where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to grab the item
        Item grabbedItem = currentRoom.getItem(direction);

        if (grabbedItem == null) {
            System.out.println("There is no item!"); // direction chosen has no item 
        }
        else {
            System.out.println("Grabbed " + grabbedItem.getInfo()); //output info
            backpack.add(grabbedItem); //add to backpack 
            
        }
    }

    public static void main(String[] args){
        Game zuul = new Game();
        zuul.play();
    }





}
