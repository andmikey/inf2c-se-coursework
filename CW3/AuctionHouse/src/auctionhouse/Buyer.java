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
public class Buyer {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private ArrayList<Lot> interestedLots;

    private String name;
    private String bankAccount;
    private String bankAuthCode;

    private AuctionHouse auctionhouse;

    public Buyer (String name, String bankAccount, String bankAuthCode, AuctionHouse auctionhouse) {
	this.name = name;
	this.bankAccount = bankAccount;
	this.bankAuthCode = bankAuthCode;
	this.auctionhouse = auctionhouse;
    }
    
    public ArrayList<CatalogueEntry> viewCatalogue () {
        return null;
    }
    
    public Status bidOnLot (Bid bid) {
        return null;
    }
    
    public Status markInterestInLot () {
        return null;
    }
    
    public Status receiveMessage () {
        return null;
    }

    public String getBankAccount () {
	return this.bankAccount;
    }

    public String getBankAuthCode () {
	return this.bankAuthCode;
    }
    
}
