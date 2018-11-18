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

    private ArrayList<Lot> lotsOwned;

    private String name;
    private String bankAccount;

    private AuctionHouse auctionhouse;
    
    public Seller (String name, String bankAccount, AuctionHouse auctionhouse) {
	this.name = name;
	this.bankAccount = bankAccount;
	this.auctionhouse = auctionhouse;
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
