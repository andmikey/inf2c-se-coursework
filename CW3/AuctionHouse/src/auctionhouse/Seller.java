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
public class Seller {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    // Mocked until Lot is implemented
    //private ArrayList<Lot> lotsOwned;

    // Mocked until Lot is implemented
    //public Status addLot (Lot lot) {
    public Status addLot () {
        return null;
    }
    
    public Status receiveMessage (
            String description, 
            Integer uniqueId, 
            Money reservePrice) {
        return null;
    }
    
}
