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
public class Client extends RegisteredUser {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private String bankAuthCode;
    private String bankAccount;

    public Client (String username, String address, AuctionHouse auctionhouse,
                   String bankAuthCode, String bankAccount) {
        super(username, address, auctionhouse);
        logger.fine("Creating new client: " +
                    "\nusername: " + username +
                    "\naddress: " + address +
                    "\nauth code: " + bankAuthCode +
                    "\nbank acc: " + bankAccount);
        this.bankAuthCode = bankAuthCode;
        this.bankAccount = bankAccount;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public String getBankAuthCode () {
        return this.bankAuthCode;
    }
    
}


