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
    public CatalogueEntry entry;

    public Lot(Seller seller, int id, String description, Money reservePrice) {
	this.seller = seller;
	this.uniqueId = id;
	this.description = description;
	this.reservePrice = reservePrice;
	this.status = LotStatus.UNSOLD;
	this.interestedBuyers = null;
	this.currentBid = null;
        this.currentPrice = new Money("0.0");
        this.entry = new CatalogueEntry(this.uniqueId, this.description, this.status);
    }
    
    public Status receiveBid (Bid bid) {
        if (this.status != LotStatus.IN_AUCTION) {
            return Status.error("Cannot place bid on a lot which is not currently in auction");
        }

        Money bidValue = bid.value;
        Bid.BidType bidType = bid.type;

        if (bidType == Bid.BidType.INCREMENT) {
            this.currentPrice = this.currentPrice.add(bid.value);
            this.currentBid = bid;
        }
        else if (bidType == Bid.BidType.JUMP) {
            if (bid.value.compareTo(this.currentPrice) < 1) {
                return Status.error("Bid must be greater than current price " +
                                    this.currentPrice.toString());
            }
            this.currentPrice = bid.value;
            this.currentBid = bid;
        }

        return Status.OK();
    }
    
    public Status open () {
        if (this.status != LotStatus.UNSOLD) {
            return Status.error("Cannot open a lot which is in a status other than unsold");
        }
        this.setStatus(LotStatus.IN_AUCTION);
        return Status.OK();
    }
    
    public Status close () {
        if (this.status != LotStatus.IN_AUCTION) {
            return Status.error("Cannot open a lot that is not currently in auction");
        }

        if (this.currentPrice.compareTo(this.reservePrice) < 0) {
            // Did not meet reserve price
            this.setStatus(LotStatus.UNSOLD);
        } else {
            // Did meet reserve price
            this.setStatus(LotStatus.SOLD);
        }
        
        this.setStatus(LotStatus.SOLD);

        return Status.OK(); 
    }

    public Status payment_failed() {
        if (this.status != LotStatus.SOLD) {
            return Status.error("Lot not in SOLD state");
        }
        
        this.setStatus(LotStatus.SOLD_PENDING_PAYMENT);
        return Status.OK();
    }
    
    public ArrayList<Buyer> getInterestedBuyers () {
        return this.interestedBuyers;
    }

    public Seller getSeller () {
        return this.seller;
    }

    public void setStatus (LotStatus newStatus) {
        this.status = newStatus;
        if (this.entry != null) this.entry.status = newStatus;
    }

}
