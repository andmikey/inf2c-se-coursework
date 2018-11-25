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
        logger.fine("Receive bid: " + bid);
        if (this.status != LotStatus.IN_AUCTION) {
            logger.fine("Lot status is not in auction, cannot place bid");
            return Status.error("Cannot place bid on a lot which is not currently in auction");
        }

        Money bidValue = bid.value;
        Bid.BidType bidType = bid.type;
        
        // Check if buyer is in the list of interested buyers
        Buyer buyer = bid.buyer;
        if (!this.interestedBuyers.contains(buyer)) {
            logger.fine("Buyer not in list of interested buyers, cannot place bid");
            return Status.error("Buyer is not interested in lot, cannot place bid");
        }
        
        if (bidType == Bid.BidType.INCREMENT) {
            logger.fine("Processing increment bid");
            this.currentPrice = this.currentPrice.add(bid.value);
            this.currentBid = bid;
        }
        else if (bidType == Bid.BidType.JUMP) {
            logger.fine("Processing jump bid");
            this.currentPrice = bid.value;
            this.currentBid = bid;
        }

        return Status.OK();
    }
    
    public Status open (Auctioneer auctioneer) {
        logger.fine("Open lot");
        
        if (this.status != LotStatus.UNSOLD) {
            logger.fine("Lot status is not unsold, cannot open");
            return Status.error("Cannot open a lot which is in a status other than unsold.");
        }
        if (auctioneer == null) {
            logger.fine("Auctioneer instance is null");
            return Status.error("Cannot open an auction without providing an auctioneer instance.");
        }
        if (this.interestedBuyers.isEmpty()) {
            logger.fine("Auction has no interested buyers");
            return Status.error("Cannot open an auction with no interested buyers.");
        }
        
        this.setStatus(LotStatus.IN_AUCTION);
        this.auctioneer = auctioneer; 
        this.currentBid = null;
        return Status.OK();
    }
    
    public Status close (Auctioneer auctioneer) {
        if (this.status != LotStatus.IN_AUCTION) {
            logger.fine("Lot is not currently in auction state");
            return Status.error("Cannot close a lot that is not currently in auction");
        }
        
        if (this.auctioneer != auctioneer) {
            logger.fine("Lot open auctioneer is not lot close auctioneer");
            return Status.error("Cannot close a lot if you are not the auctioneer that opened it.");
        }

        if (this.currentPrice.compareTo(this.reservePrice) < 0) {
            logger.fine("Lot did not meet reserve price");
            // Did not meet reserve price
            this.setStatus(LotStatus.UNSOLD);
        } else {
            logger.fine("Lot met reserve price");
            // Did meet reserve price
            this.setStatus(LotStatus.SOLD);
        }

        if (this.currentBid == null) {
            logger.fine("No bids placed on item");
            this.setStatus(LotStatus.UNSOLD);
        }
        
        // TODO do we need to zero out any variables here?
        return Status.OK(); 
    }

    public Status payment_failed() {
        if (this.status != LotStatus.SOLD) {
            logger.fine("Lot not in sold status, cannot set to payment failed");
            return Status.error("Lot not in SOLD state");
        }
        
        logger.fine("Setting lot status to SOLD_PENDING_PAYMENT");
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
        logger.fine("Adding interested buyer " + buyer.username);
        // Cannot mark interest in lot if already interested
        if (this.interestedBuyers.contains(buyer)) {
            logger.fine("Buyer already marked as interested in lot");
            return Status.error("Buyer already marked as interested in lot");
        }

        // Cannot mark interest in lot if lot in not in auction or unsold
        if (!(this.status == LotStatus.IN_AUCTION || this.status == LotStatus.UNSOLD)) {
            logger.fine("Cannot mark interest in SOLD lot");
            return Status.error("Cannot mark interest in SOLD lot");
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

    public int getID() {
        return this.uniqueId;
    }
}
