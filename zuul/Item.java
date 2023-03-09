/**
 *  The item class. each item has a description, name and weight. 
 * 
 * there are two getter functions to retreive item data
 * 
 * the item constructor is used in the creation of rooms where each room has one or more items in a certain direction. 
 * 
 * @author James Runtas
 * @version March 9th 2023
 * * Student Number 101109175
 */


public class Item {
    private String description;
    private Double weight;
    private String name;
  

    //Item Constructor
    public Item(String iDescription, Double iWeight, String iName) {
        this.description = iDescription;
        this.weight = iWeight;
        this.name = iName;
    }


    //print statement for all item information
    public void getItem(Item something){
        System.out.println("> " + this.name + ": " + this.description + " weight: " + this.weight);
    }

    public String getName(Item something){
        return this.name;
    }

    //return string for item description 
    public String getInfo() {
        return this.name + ": " + this.description + " weight: " + this.weight;
    }

}

