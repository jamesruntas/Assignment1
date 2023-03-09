/**
 *  The beamer class. each beamer is a sub class of item. includes
 * a charge and a room attribute 
 * 
 * The beamer item is a device that can be charged and fired. When you charge 
 * the beamer, it memorizes the current room. When you fire the beamer, it transports you 
*  immediately back to the room it was charged in. 
 * 
 * Two constructors using super
 * mutliple getters
 * charge function and fire function.
 * 
 * 
 * 
 * the item constructor is used in the creation of rooms where each room has one or more items in a certain direction. 
 * 
 * @author James Runtas
 * @version March 9th 2023
 * * Student Number 101109175
 */
public class Beamer extends Item {
    
    private Boolean charge;     //charge value is either true or false
    private Room chargeRoom;    // the beamer holds a room attribute where it was charged
    
    /*
     * Constructor one, doesnt include a room. sets charge to false
     */
    public Beamer(final String iDescription, final Double iWeight, final String iName) {
        super(iDescription, iWeight, iName);
        this.charge = false;
    }

    /*
     * Constructor two, includes a room determined by a constructor call in game and sets charge to true
     */
    public Beamer(final String iDescription, final Double iWeight, final String iName, final Room ichargeRoom) {
        super(iDescription, iWeight, iName);
        this.charge = true;
        this.chargeRoom = ichargeRoom;
    }



    /*
     * return value of beamer charge
     */
    public boolean getCharge(){
        return this.charge;
    }

    /*
     * return value of beamer room
     */
    public Room getChargeRoom(){
        return this.chargeRoom;
    }
    

    /*
     * charges the beamer. uses a Room object that is now set to this beamer and is now charged
     */
    public void chargeBeamer(final Room iChargeRoom){
        this.charge = true;
        this.chargeRoom = iChargeRoom;
    }


    /*
     * beamer fired and charge is reset. room funcitonality done in game class within fire()
     */
    public void fireBeamer(){
        this.charge = false;
    }

}
