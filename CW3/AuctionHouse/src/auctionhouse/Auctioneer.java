/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author djt
 *
 */
public class Auctioneer extends RegisteredUser {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();
    public boolean isActive;
    
    private ArrayList<Lot> assignedLots;

    public Auctioneer (String username, String address, AuctionHouse auctionhouse) {
        super(username, address, auctionhouse);
        this.assignedLots = new ArrayList<Lot>();
        this.isActive = false;
    }
    
    public Status openAuction (Lot lot) {
        return null;
    }
    
    public Status closeAuction (Lot lot) {
        return null;
    }
    
}

