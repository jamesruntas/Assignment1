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
        Room outside, theatre, pub, lab, office, dorm; //6 rooms 
      
        // create the rooms and their identifiers 
        outside = new Room("outside the main entrance of the university", "outside");
        theatre = new Room("in a lecture theatre", "theatre");
        pub = new Room("in the campus pub", "pub");
        dorm = new Room("Lennox and Addington", "dorm");
        lab = new Room("in a computing lab", "lab");
        office = new Room("in the computing admin office", "office");
        
        //create the item objects 
        Beamer beamer = new Beamer("Beamer device", 10.0, "Beamer");
        Item chestArmor = new Item("Level One bronze Armor", 50.0, "Chest Armor");
        Item helmet = new Item("Level One bronze Helmet", 50.0, "Helmet");
        Item sword = new Item("Level One wooden Sword", 50.0, "Sword");
        Item shield = new Item("Level one wooden shield ", 50.0, "Shield");
        Item bigChungus = new Item("a huge chungus, grants +500 carrying capacity", 500.0, "Big Chungus");
        Item cookie = new Item("edible cookie, cure hunger",1.0, "Cookie");

        // initialise room exits and linked to the next room
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        theatre.setExit("west", outside);
        pub.setExit("south", dorm);
        pub.setExit("east", outside);
        dorm.setExit("west", outside);
        dorm.setExit("north", pub);
        lab.setExit("north", outside);
        lab.setExit("east", office);
        office.setExit("west", lab);

        // initialise room items, what item and where they are stashed
        outside.setItem("north", cookie);
        theatre.setItem("east", sword);
        theatre.setItem("south", cookie);
        dorm.setItem("east", beamer);
        pub.setItem("south", helmet);
        lab.setItem("south", beamer);
        lab.setItem("west", bigChungus);
        office.setItem("north", shield);
        office.setItem("east", chestArmor);


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
            eat();
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("backpack")) {
            backpack();
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge();
        }
        else if (commandWord.equals("fire")) {
            fire();
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
    
    /**Player eats cookie to reset hunger 
    * @param command The command to be processed
    * @return void, only prints
    */
    private void eat() 
    {
        Boolean cookieFlag = false;   //a flag that helps keep track of if a cookie was found to eat
        for (Item item : backpack){
            if (item.getName(item)=="Cookie"){
                //change the players hungry value
                Game.this.hungry = false;
                System.out.println("Cookie eaten, Player hunger status: " + Game.this.hungry);

                //remove cookie from backpack
                backpack.remove(item);
                cookieFlag = true;
                break;

            }
        }
        if (cookieFlag == false){  //if the code above did not trigger eating a cookie
            System.out.println("no cookie to eat. Hungry: "+ Game.this.hungry);
        }
        

    }


    /**
     * Try to grab an item in a certain direction. If there is an item, add to backpack and show item info
     * otherwise print an error message. item will not be in same direction as an exit, therefore player always stays in same room
     * 
     * only take items when the player has eaten a cookie. this is determined by our hunger 'meter'.
     * I will check the hunger meter to see if the player has found and eaten the cookie
     * I will also check the users backpack to see if they forgot to eat the cookie. 
     * 
     * @param command The command to be processed
     */
    private void take(Command command) 
    {
        if(backpack.size() >= 5){
            System.out.println("You are carrying to much!");
            return;
        }

        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go to pick up item...
            System.out.println("take where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to grab the item
        Item grabbedItem = currentRoom.getItem(direction);

        if (grabbedItem == null) {
            System.out.println("There is no item!"); // direction chosen has no item 
        }
        else {
            if(grabbedItem.getName(grabbedItem)!="Cookie"){
                if(Game.this.hungry == true){ //player must eat cookie before taking items
                    System.out.println("You must find and eat a cookie before picking up items. Hint: Outside north, theatre south");
                        for (Item item : backpack){  //help the user by looking through thier items to find a cookie
                            if(item.getName(item)=="Cookie"){
                                System.out.println("I see you have a cookie in your backpack, use 'eat' command");
                            }
                        }
                    return;
                }
            }
            
            System.out.println("Grabbed " + grabbedItem.getInfo()); //output info
            backpack.add(grabbedItem); //add to backpack 
            
        }
    }


    /*
     * Drops item in backpack using simple index numbering system
     * User should use backpack command in order to get the index of each item 'drop 1'
     * 
     */
    public void drop(Command command){
        String userIndex = command.getSecondWord();
        int itemIndex = convert(userIndex);
        itemIndex--;
        try {
            backpack.remove(itemIndex);
            System.out.println("Item dropped!");
          }
          catch(Exception e) {
            
          }
          

    }

    /*
     * Simple function from geeksforgeeks.com to convert string to int
     * used for converting user inputed backpack item index to int
     */
    public static int convert(String str)
    {
        int val = 0;
  
        // Convert the String
        try {
            val = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
  
            // This is thrown when the String
            // contains characters other than digits
            System.out.println("Invalid String");
        }
        return val;
    }

    /*
     * Returns a text output of all acquired items in the players backpack.
     * an index i is used to number all items, making it easier to drop items. 'drop i'
     * 
     */
    public void backpack(){
        int i = 1;
        for (Item item : backpack){
            System.out.println(i + ": "+ item.getInfo()); //output items grabbed
            i++;
        }
           
    }

    public void charge(){
        for (Item i : backpack){
            if(i.getName(i)=="Beamer"){
                Beamer iBeamer = (Beamer)i;
                iBeamer.chargeBeamer(currentRoom);
                System.out.println("Beamer has been charged in room: " + currentRoom.getShortDescription()) ;
            }
            break;
        }
    }

    public void fire(){
        for (Item i : backpack){
            if(i.getName(i)=="Beamer"){
                Beamer iBeamer = (Beamer)i;
                if (iBeamer.getCharge()==true){
                    System.out.println("Firing Beamer");
                    iBeamer.fireBeamer();
                    this.currentRoom = iBeamer.getChargeRoom();
                    System.out.println("Now in beamer charged room: " + currentRoom.getShortDescription());

                }
                else{
                    System.out.println("Beamer not charged");
                    return;
                }
            }
            break;
        }

    }

    public static void main(String[] args){
        Game zuul = new Game();
        zuul.play();
    }





}
