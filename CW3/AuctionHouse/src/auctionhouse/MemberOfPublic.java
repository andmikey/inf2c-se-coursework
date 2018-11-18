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
public class MemberOfPublic extends Actor {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    public MemberOfPublic (String address, AuctionHouse auctionhouse) {
        super(address, auctionhouse);
    }
    
}

