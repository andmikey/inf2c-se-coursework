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
public class Buyer extends Client {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private ArrayList<Lot> interestedLots;

    public Buyer (String username, String address, AuctionHouse auctionhouse, String bankAuthCode, String bankAccount) {
        super(username, address, auctionhouse, bankAuthCode, bankAccount);
        this.interestedLots = new ArrayList<Lot>();
    }
    
    public Status bidOnLot (Bid bid) {
        return null;
    }
    
    public Status markInterestInLot () {
        return null;
    }
    
}
