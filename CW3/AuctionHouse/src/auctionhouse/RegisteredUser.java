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
public class RegisteredUser extends Actor {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    public String username;

    public RegisteredUser (String username, String address, AuctionHouse auctionhouse) {
        super(address, auctionhouse);
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}

