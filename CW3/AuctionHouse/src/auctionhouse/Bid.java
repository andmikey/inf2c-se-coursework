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

    private Lot lot;
    private Buyer buyer;
    private Money value;
    private BidType type;

    public static enum BidType {
        INCREMENT, 
        JUMP, 
        }
}
