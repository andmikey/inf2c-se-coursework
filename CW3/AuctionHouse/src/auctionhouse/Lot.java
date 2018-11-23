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
    private Auctioneer auctioneer;
    
    // Commented out until CatalogueEntry is defined
    public CatalogueEntry entry;

    public Lot(Seller seller, int id, String description, Money reservePrice) {
        this.seller = seller;
        this.uniqueId = id;
        this.description = description;
        this.reservePrice = reservePrice;
        this.status = LotStatus.UNSOLD;
        this.interestedBuyers = new ArrayList<Buyer>();
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
        // TODO check if buyer is an interested buyer

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
    
    public Status open (Auctioneer auctioneer) {
        if (this.status != LotStatus.UNSOLD) {
            return Status.error("Cannot open a lot which is in a status other than unsold.");
        }
        if (this.auctioneer == null) {
            return Status.error("Cannot open an auction without providing an auctioneer instance.");
        }
        if (this.interestedBuyers.isEmpty()) {
            return Status.error("Cannot open an auction with no interested buyers.");
        }
        this.setStatus(LotStatus.IN_AUCTION);
        this.auctioneer = auctioneer;
        return Status.OK();
    }
    
    public Status close (Auctioneer auctioneer) {
        if (this.status != LotStatus.IN_AUCTION) {
            return Status.error("Cannot open a lot that is not currently in auction");
        }
        
        if (this.auctioneer != auctioneer) {
            return Status.error("Cannot close a lot if you are not the auctioneer that opened it.");
        }

        if (this.currentPrice.compareTo(this.reservePrice) < 0) {
            // Did not meet reserve price
            this.setStatus(LotStatus.UNSOLD);
        } else {
            // Did meet reserve price
            this.setStatus(LotStatus.SOLD);
        }
        
        this.setStatus(LotStatus.SOLD);
        // Reset auctioneer to null, as we may go on sale multiple times
        this.auctioneer = null;
        // Reset current bid to null
        // TODO do we need to zero out the other variables too?
        this.currentBid = null;
        
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

    public LotStatus getStatus () {
        return this.status;
    }

    public Auctioneer getAuctioneer () {
        return this.auctioneer;
    }

    public void setStatus (LotStatus newStatus) {
        this.status = newStatus;
        if (this.entry != null) this.entry.status = newStatus;
    }

    public Status addInterestedBuyer(Buyer buyer) {
        if (this.interestedBuyers.contains(buyer)) {
            return Status.error("Buyer already marked as interested in lot");
        }

        this.interestedBuyers.add(buyer);

        return Status.OK();
    }

    public Buyer getWinner () {
        return this.currentBid.buyer;
    }

    public Money getPrice () {
        return this.currentPrice;
    }

    public Buyer getBuyerOfCurrentBid() {
        return this.currentBid.buyer; 
    }
}
