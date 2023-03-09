public class Beamer extends Item {
    
    private Boolean charge;
    private Room chargeRoom; 
    
    public Beamer(final String iDescription, final Double iWeight, final String iName) {
        super(iDescription, iWeight, iName);
        this.charge = false;
    }

    public Beamer(final String iDescription, final Double iWeight, final String iName, final Room ichargeRoom) {
        super(iDescription, iWeight, iName);
        this.charge = true;
        this.chargeRoom = ichargeRoom;
    }


    public boolean getCharge(){
        return this.charge;
    }
    public Room getChargeRoom(){
        return this.chargeRoom;
    }
    
    public void chargeBeamer(final Room iChargeRoom){
        this.charge = true;
        this.chargeRoom = iChargeRoom;
    }

    public void fireBeamer(){
        this.charge = false;
    }

}
