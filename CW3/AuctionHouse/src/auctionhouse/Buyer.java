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
public class Buyer {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    // Mocked until Lot is implemented
    //private ArrayList<Lot> interestedLots;

    public ArrayList<CatalogueEntry> viewCatalogue () {
        return null;
    }
    
    public Status bidOnLot () {
        return null;
    }
    
    public Status markInterestInLot () {
        return null;
    }
    
    public Status receiveMessage () {
        return null;
    }
    
}
