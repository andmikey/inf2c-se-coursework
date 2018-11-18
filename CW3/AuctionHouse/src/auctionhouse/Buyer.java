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

    private String name;
    private String bankAccount;
    private String bankAuthCode;

    private AuctionHouse auctionhouse;

    public Buyer (String username, String address, AuctionHouse auctionhouse, String bankAuthCode, String bankAccount) {
        super(username, address, auctionhouse, bankAuthCode, bankAccount);
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
