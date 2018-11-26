/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dylan Thinnes
 * @author Michael Andrejczuk
 *
 */
public class Seller extends Client {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private ArrayList<Lot> lotsOwned;
    
    public Seller (String username, String address, AuctionHouse auctionhouse, String bankAuthCode, String bankAccount) {
        super(username, address, auctionhouse, bankAuthCode, bankAccount);
        this.lotsOwned = new ArrayList<Lot>();
    }
    
    public Status addLot (Lot lot) {
        return null;
    }
    
}
