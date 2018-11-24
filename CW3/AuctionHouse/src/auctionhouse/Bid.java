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
public class Bid {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    public Lot lot;
    public Buyer buyer;
    public Money value;
    public BidType type;

    public Bid(Lot lot, Buyer buyer, Money value, BidType type) {
        logger.fine("Creating lot with information: " +
                    "\nLot: " + lot.getID() +
                    "\nBuyer: " + buyer.getUsername() +
                    "\nMoney: " + value.toString() +
                    "\nType: " + type);
                    
        this.lot = lot;
        this.buyer = buyer;
        this.value = value;
        this.type = type;
    }

    public static enum BidType {
        INCREMENT, 
        JUMP, 
    }


}
