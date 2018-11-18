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
    private Money currentPrice;
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
        this.currentPrice = new Money("0.0");
    }
    
    public Status receiveBid (Bid bid) {
        if (this.status != LotStatus.IN_AUCTION) {
            return Status.error("Cannot place bid on a lot which is not currently in auction");
        }
    }
    
    public Status open () {
        if (this.status != LotStatus.UNSOLD) {
            return Status.error("Cannot open a lot which is in a status other than unsold");
        }
        this.status = LotStatus.IN_AUCTION;
        return Status.OK();
    }
    
    public Status close (boolean payment_succeeded) {
        if (this.status != LotStatus.IN_AUCTION) {
            return Status.error("Cannot open a lot that is not currently in auction");
        }

        // Did not meet reserve price
        if (this.currentPrice < this.reservePrice) {
            this.status = LotStatus.UNSOLD;
        }
        
        if (payment_succeeded) {
            this.status = LotStatus.SOLD;
        }
        else {
            this.status = LotStatus.SOLD_PENDING_PAYMENT;
        }

        return Status.OK(); 
    }

    public ArrayList<Buyer> getInterestedBuyers () {
        return this.interestedBuyers;
    }

    public Seller getSeller () {
        return this.seller;
    }

}
