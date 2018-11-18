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
public class Seller extends Client {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private ArrayList<Lot> lotsOwned;

    private String name;
    private String bankAccount;

    private AuctionHouse auctionhouse;
    
    public Seller (String username, String address, AuctionHouse auctionhouse, String bankAuthCode, String bankAccount) {
        super(username, address, auctionhouse, bankAuthCode, bankAccount);
    }
    
    public Status addLot (Lot lot) {
        return null;
    }
    
    public Status receiveMessage (
            String description, 
            Integer uniqueId, 
            Money reservePrice) {
        return null;
    }

    public String getBankAccount() {
	return this.bankAccount;
    }
    
}
