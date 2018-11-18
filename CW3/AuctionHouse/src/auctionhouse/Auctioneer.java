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
public class Auctioneer {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private ArrayList<Lot> assignedLots;
    private AuctionHouse auctionhouse;

    public Auctioneer (AuctionHouse auctionhouse) {
	this.auctionhouse = auctionhouse;
    }
    
    public Status openAuction (Lot lot) {
        return null;
    }
    
    public Status closeAuction (Lot lot) {
        return null;
    }
    
    public Status receiveMessage () {
        return null;
    }
    
}

