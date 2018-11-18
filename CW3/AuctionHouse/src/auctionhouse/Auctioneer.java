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

    private ArrayList<Lot> assignedLots;

    public Auctioneer (String username, String address, AuctionHouse auctionhouse) {
        super(username, address, auctionhouse);
    }
    
    public Status openAuction (Lot lot) {
        return null;
    }
    
    public Status closeAuction (Lot lot) {
        return null;
    }
    
}

