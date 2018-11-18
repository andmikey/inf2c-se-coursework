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
public class Lot {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private int uniqueId;
    private Seller seller;
    private String description;
    private Money reservePrice;
    private LotStatus status;
    private ArrayList<Buyer> interestedBuyers;
    private Bid currentBid;
    // Commented out until CatalogueEntry is defined
    //private CatalogueEntry entry;

    public Lot(Seller seller, int id, String description, Money reservePrice) {
	this.seller = seller;
	this.uniqueId = id;
	this.description = description;
	this.reservePrice = reservePrice;
	this.status = LotStatus.UNSOLD;
	this.interestedBuyers = null;
	this.currentBid = null;
    }
    
    public Status receiveBid (Bid bid) {
        return null;
    }
    
    public Status open () {
        return null;
    }
    
    public Status close () {
        return null;
    }
    
}
