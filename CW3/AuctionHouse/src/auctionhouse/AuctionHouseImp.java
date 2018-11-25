/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author pbj
 *
 */
public class AuctionHouseImp implements AuctionHouse {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();
    
    private String startBanner(String messageName) {
        return  LS 
          + "-------------------------------------------------------------" + LS
          + "MESSAGE IN: " + messageName + LS
          + "-------------------------------------------------------------";
    }

    private ArrayList<Buyer> buyers;
    private ArrayList<Seller> sellers;
    private ArrayList<Auctioneer> auctioneers;
    private HashMap<Integer, Lot> lots;
    private HashMap<String, Actor> addressBook;
    private ArrayList<CatalogueEntry> catalogue;

    private Parameters parameters;
   
    public AuctionHouseImp(Parameters parameters) {
        logger.fine("Creating new auctionhouse object");
        this.parameters = parameters;

        this.buyers = new ArrayList<Buyer>();
        this.sellers = new ArrayList<Seller>();
        this.auctioneers = new ArrayList<Auctioneer>();
        this.lots = new HashMap<Integer, Lot>();

        this.addressBook = new HashMap<String, Actor>();
        this.catalogue = new ArrayList<CatalogueEntry>();
    }

    public Status registerBuyer(
            String username,
            String address,
            String bankAccount,
            String bankAuthCode) {
        logger.fine(startBanner("registerBuyer " + username));
        String baseMessage = "registerBuyer " + username + ": ";
        
        if (username == null) {
            return Status.error("Cannot register a buyer with a null username");
        }
        else if (address == null) {
            return Status.error("Cannot register a buyer with a null address");
        }
        else if (bankAccount == null) {
            return Status.error("Cannot register a buyer with empty bank account");
        }
        else if (bankAuthCode == null) {
            return Status.error("Cannot register a buyer with no bank auth code");
        }
        
        logger.fine(baseMessage + "checking address is not duplicate");
        Actor a = addressBook.get(address);
        if (a != null) {
            logger.fine(baseMessage + "address is duplicate");
            return Status.error("Address " + address + " already belongs to an " +
                                "existing user, cannot register buyer");
        }
        
        logger.fine(baseMessage + "checking username is not duplicate");        
        Buyer existingBuyer = findBuyer(username);        
        if (existingBuyer != null) {
            logger.fine(baseMessage + "username already taken");
            return Status.error("Username " + username + " already belongs to an " +
                                "existing buyer, cannot register buyer");
        }

        logger.fine(baseMessage + "creating Buyer object");        
        Buyer buyer = new Buyer(username, address, this, bankAuthCode, bankAccount);

        logger.fine(baseMessage + "adding to buyer list and address book");        
        buyers.add(buyer);
        addressBook.put(address, buyer);
        
        return Status.OK();
    }

    public Status registerSeller(
            String username,
            String address,
            String bankAccount) {
        logger.fine(startBanner("registerSeller " + username));
        String baseMessage = "registerSeller " + username + ": ";

        logger.fine(baseMessage + "checking no arguments are null");
        if (username == null) {
            return Status.error("Cannot register a seller with a null username");
        }
        else if (address == null) {
            return Status.error("Cannot register a seller with a null address");
        }
        else if (bankAccount == null) {
            return Status.error("Cannot register a seller with empty bank account");
        }
        
        logger.fine(baseMessage + "checking address is not duplicate");
        Actor a = addressBook.get(address);
        if (a != null) {
            logger.fine(baseMessage + "address is duplicate");
            return Status.error("Address " + address + " already belongs to an " +
                                "existing user, cannot register seller");
        }

        logger.fine(baseMessage + "checking username is not duplicate");        
        Seller existingSeller = findSeller(username);        
        if (existingSeller != null) {
            logger.fine(baseMessage + "username is duplicate");
            return Status.error("Username " + username + " already belongs to an " +
                                "existing seller, cannot register seller");
        }

        logger.fine(baseMessage + "creating Seller object");        
        Seller seller = new Seller(username, address, this, null, bankAccount);

        logger.fine(baseMessage + "adding to seller list, address book");        
        sellers.add(seller);
        addressBook.put(address, seller);
        
        return Status.OK();      
    }

    public Status addLot(
            String sellerName,
            int number,
            String description,
            Money reservePrice) {
        logger.fine(startBanner("addLot " + sellerName + " " + number));
        String baseMessage = "addLot " + number + " " + description + ": ";

        // Check no attributes are null
	    if (sellerName == null) {
	        return Status.error("Cannot add a lot without a seller");
	    }
	    else if (description == null) {
	        return Status.error("Cannot add a lot without a description");
	    }
	    else if (reservePrice == null) {
	        return Status.error("Cannot add a lot without a reserve price");
	    }

        // Check reserve price isn't less than zero
        Money zero = new Money("0.0");
        if (reservePrice.compareTo(zero) < 0) {
            logger.fine(baseMessage + "reserve price of lot is less than zero");
            return Status.error("Reserve price of lot cannot be less than zero");
        }
        
	    // Check there's no catalogue entries with the same number
	    // Note that we can't use .equals as it compares number, desc, *and* status
	    // which would allow for two entries with the same number but different statuses
	    for (CatalogueEntry entry : catalogue) {
	        if (entry.lotNumber == number) {
                logger.fine(baseMessage + "lot has duplicate number");
	    	return Status.error("Cannot add a lot with the same number as " +
	    		     " an existing lot. Conflicting lot: \n " + entry.toString());
	        }
	    }

        Seller seller = findSeller(sellerName);

	    if (seller == null) {
            logger.fine(baseMessage + "can't find associated seller " + sellerName);
	        return Status.error("Cannot find seller of username " + sellerName +
	    			", so lot cannot be added");
	    }

        logger.fine(baseMessage + "create lot and add to lot list, catalogue");
        Lot newLot = new Lot(seller, number, description, reservePrice);
        
        // Insert lot into our hashmap for future referencing
        this.lots.put(number, newLot);

        // Note that by associating an entry with a lot by passing it in
        // the Lot can keep the catalogue entry up to date by mutating it.
	    catalogue.add(newLot.entry);
        
        return Status.OK();    
    }

    public Seller findSeller(String name) {
        Seller fUser = null;
        for (Seller user : this.sellers) {
            if (user.getUsername() == name) {
                fUser = user;
                break;
            }
        }
        return fUser;
    }
    
    public Buyer findBuyer(String name) {
        Buyer fUser = null;
        for (Buyer user : this.buyers) {
            if (user.getUsername() == name) {
                fUser = user;
                break;
            }
        }
        return fUser;
    }
    
    public Auctioneer findAuctioneer(String name) {
        Auctioneer fUser = null;
        for (Auctioneer user : this.auctioneers) {
            if (user.getUsername() == name) {
                fUser = user;
                break;
            }
        }
        return fUser;
    }
    

    public List<CatalogueEntry> viewCatalogue() {
        logger.fine(startBanner("viewCatalogue"));
        
        logger.fine("Catalogue: " + catalogue.toString());
        Collections.sort(catalogue, new Comparator<CatalogueEntry>() {
                @Override
                public int compare(CatalogueEntry e1, CatalogueEntry e2) {
                    int comp = Integer.compare(e1.lotNumber, e2.lotNumber);
                    return comp;
                }
            });
        return catalogue;
    }

    public Status noteInterest(
            String buyerName,
            int lotNumber) {
        logger.fine(startBanner("noteInterest " + buyerName + " " + lotNumber));
        String baseMessage = "noteInterest " + buyerName + " " + lotNumber + ": ";
        
        // Make sure lot exists
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            logger.fine(baseMessage + "lot with number not found");
            return Status.error("Lot with number " + lotNumber + " not found.");
        }

        // Make sure buyer exists
        Buyer buyer = findBuyer(buyerName);        
        if (buyer == null) {
            logger.fine(baseMessage + "buyer not found");
            return Status.error("Buyer with username " + buyerName + " not found.");
        }

        Status lotAdd = lot.addInterestedBuyer(buyer);
        if (lotAdd.kind == Status.Kind.ERROR) {
            logger.fine(baseMessage + "lot returned an error");
            return lotAdd;
        }
        
        return Status.OK();   
    }

    public Status openAuction(
            String auctioneerName,
            String auctioneerAddress,
            int lotNumber) {
        logger.fine(startBanner("openAuction " + auctioneerName + " " + lotNumber));
        String baseMessage = "openAuction: " + auctioneerName + " " + lotNumber + ": ";
        
        // Make sure lot exists
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            logger.fine(baseMessage + "lot number not found");
            return Status.error("Lot with number " + lotNumber + " not found.");
        }

        Auctioneer auctioneer = this.findAuctioneer(auctioneerName);
        // Make sure auctioneer exists
        if (auctioneer == null) {
            logger.fine(baseMessage + "auctioneer not found; adding to list");
            Auctioneer newAuctioneer = new Auctioneer(auctioneerName, auctioneerAddress, this);
            this.auctioneers.add(newAuctioneer);
            auctioneer = newAuctioneer;
        }

        // Check auctioneer is not already running an auction
        if (auctioneer.isActive) {
            return Status.error("Auctioneer already involved in an auction");
        }

        
        // Make sure lot can be opened
        Status openingAttempt = lot.open(auctioneer);
        if (openingAttempt.kind == Status.Kind.ERROR) {
            logger.fine(baseMessage + "opening attempt failed");
            // If lot opening fails, return the error that it failed with
            return openingAttempt;
        }

        // Auctioneer is now involved in an auction
        auctioneer.isActive = true;

        logger.fine(baseMessage + "sending out auctionOpened messages");
        
        // Message seller of lot that it has gone on auction
        Seller seller = lot.getSeller();
        String addr = seller.getAddress();
        this.parameters.messagingService.auctionOpened(addr, lotNumber);

        // Notify all interested buyers of a lot that it has gone on auction
        ArrayList<Buyer> interestedBuyers = lot.getInterestedBuyers();
        for (Buyer interestedBuyer : interestedBuyers) {
            addr = interestedBuyer.getAddress();
            this.parameters.messagingService.auctionOpened(addr, lotNumber);
        }

        return Status.OK();
    }

    public Status makeBid(
            String buyerName,
            int lotNumber,
            Money bid) {
        logger.fine(startBanner("makeBid " + buyerName + " " + lotNumber + " " + bid));
        String baseMessage = "makeBid: " + buyerName + " " + lotNumber + ": ";
 
        // Make sure lot exists
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            logger.fine(baseMessage + "lot not found");
            return Status.error("Lot with number " + lotNumber + " not found.");
        }
        
        // Bid is less than minimum increment
        // Note that this also handles negative numbers, so we don't need to check this
        if (bid.compareTo(this.parameters.increment) == -1) {
            logger.fine(baseMessage + "bid is less than minimum increment");
            return Status.error("Bid must be greater than minimum increment " +
                                this.parameters.increment.toString());
        }

        // Difference between bid and current price is less than minimum increment
        Money difference = bid.subtract(lot.getPrice());
        if (difference.compareTo(this.parameters.increment) == -1) {
            logger.fine(baseMessage + "bid is less than minimum increment");
            return Status.error("Bid must be greater than minimum increment " +
                                this.parameters.increment.toString());            
        }
        
        // Identify buyer
        Buyer buyer = findBuyer(buyerName);        
        if (buyer == null) {
            logger.fine(baseMessage + "buyer not found");
            return Status.error("Buyer with username " + buyerName + " not found.");
        }

        // Decide if bid is jump or increment
        Bid.BidType bidType = null;
        if (bid.lessEqual(lot.getPrice())) {
            return Status.error("Bid must be greater than the current price of the lot");
        }
        else {
            bidType = Bid.BidType.JUMP;
        }
        // Create the bid object
        Bid bid_obj = new Bid(lot, buyer, bid, bidType);
        
        // Try to make bid on lot
        // Return early if bid failed
        Status bidAttempt = lot.receiveBid(bid_obj);
        if (bidAttempt.kind == Status.Kind.ERROR) {
            logger.fine(baseMessage + "lot receiveBid returned error");
            // If lot closing fails, return the error that it failed with
            return bidAttempt;
        }

        logger.fine(baseMessage + "sending out bid notifications");
        // Message relevant parties
        Auctioneer auctioneer = lot.getAuctioneer();
        String auctioneerAddress = auctioneer.getAddress();
        this.parameters.messagingService.bidAccepted(auctioneerAddress,
                                                     lotNumber, bid);
                                                     
        Seller seller = lot.getSeller();
        String sellerAddress = seller.getAddress();
        this.parameters.messagingService.bidAccepted(sellerAddress,
                                                     lotNumber, bid);
        
        ArrayList<Buyer> buyers = lot.getInterestedBuyers();
        for (Buyer intBuyer : buyers) {
            if (intBuyer.username == buyerName) {
                continue;
            }
            String addr = intBuyer.getAddress();
            this.parameters.messagingService.bidAccepted(addr,
                                                         lotNumber, bid);
        }
        
        return Status.OK();    
    }

    public Status closeAuction(
            String auctioneerName,
            int lotNumber) {
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));
        String baseMessage = "closeAuction: " + auctioneerName + " " + lotNumber + ": ";
        logger.fine(baseMessage + "looking for lot");
        // Find lot
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            logger.fine(baseMessage + "could not find lot");
            return Status.error("Lot with number " + lotNumber + " not found.");
        }
        logger.fine(baseMessage + "found lot, continuing");
        
        // Identify relevant parties
        Auctioneer auctioneer = findAuctioneer(auctioneerName);

        logger.fine(baseMessage + "looking for auctioneer");
        // Make sure auctioneer exists
        if (auctioneer == null) {
            logger.fine(baseMessage + "auctioneer does not exist");
            return Status.error("Auctioneer not found; cannot close lot without valid auctioneer.");
        }

        // Make sure lot can be closed
        Status closingAttempt = lot.close(auctioneer);
        logger.fine(baseMessage + "lot status returned " + closingAttempt);
        if (closingAttempt.kind == Status.Kind.ERROR) {
            logger.fine(baseMessage + "lot closing failed");
            // If lot closing fails, return the error that it failed with
            return closingAttempt;
        }

        auctioneer.isActive = false;
        
        // Check if lot has been sold or unsold
        LotStatus status = lot.getStatus();
        logger.fine(baseMessage + "lot status is " + status);

        Seller seller = lot.getSeller();
        ArrayList<Buyer> interestedBuyers = lot.getInterestedBuyers();


        // If it has not been sold, notify all parties then abort
        if (status == LotStatus.UNSOLD) {
            logger.fine(baseMessage + "lot status is unsold, informing buyers and sellers");
            // Inform buyers, seller that lot is unsold
            String addr = seller.getAddress();
            this.parameters.messagingService.lotUnsold(addr, lotNumber);

            for (Buyer interestedBuyer : interestedBuyers) {
                addr = interestedBuyer.getAddress();
                this.parameters.messagingService.lotUnsold(addr, lotNumber);
            }

            // Return as NO_SALE
            return new Status(Status.Kind.NO_SALE);
        } else if (status == LotStatus.SOLD) {
            logger.fine(baseMessage + "lot status is sold");
            
            Buyer winningBuyer = lot.getBuyerOfCurrentBid();
            
            Money sold_price = lot.getPrice();
            Money buyerPremium = new Money(Double.toString(this.parameters.buyerPremium));
            Money commission = new Money(Double.toString(this.parameters.commission));
            Money buyer_pays = sold_price.add(buyerPremium);
            Money seller_pays = sold_price.subtract(commission);


            logger.fine(baseMessage + "sending payment to seller");            
            // Get seller's credentials
            String sellerAccount = seller.getBankAccount();
            
            // Try to take payment from winner
            Status sellerAttempt = this.parameters.bankingService.transfer(
                    this.parameters.houseBankAccount,
                    this.parameters.houseBankAuthCode,
                    sellerAccount,
                    seller_pays
                    );

            // If sending seller payment failed, set lot status to sold pending payment
            if (sellerAttempt.kind == Status.Kind.ERROR) {
                logger.fine(baseMessage + "payment to seller failed");
                lot.payment_failed();
                return new Status(Status.Kind.SALE_PENDING_PAYMENT);
            }

            // Get winning buyer's credentials
            String buyerAccount = winningBuyer.getBankAccount();
            String buyerAuthCode = winningBuyer.getBankAuthCode();
            
            logger.fine(baseMessage + "taking payment from buyer");            
            // Try to take payment from winner
            Status buyerAttempt = this.parameters.bankingService.transfer(
                    buyerAccount,
                    buyerAuthCode,
                    this.parameters.houseBankAccount,
                    buyer_pays);

            // If withdrawing payment failed, set lot status to sold pending payment
            if (buyerAttempt.kind == Status.Kind.ERROR) {
                logger.fine(baseMessage + "payment from buyer failed");
                lot.payment_failed();
                return new Status(Status.Kind.SALE_PENDING_PAYMENT);
            }

            logger.fine(baseMessage + "both payments succeeded, informing buyers, seller");
            // If both payments succeeded, inform buyers, seller that lot has sold
            String addr = seller.getAddress();
            this.parameters.messagingService.lotSold(addr, lotNumber);

            for (Buyer interestedBuyer : interestedBuyers) {
                addr = interestedBuyer.getAddress();
                this.parameters.messagingService.lotSold(addr, lotNumber);
            }
            
            return new Status(Status.Kind.SALE);
        }
        else {
            logger.fine(baseMessage + "lot is in unexpected state " + status);
            return Status.error("Lot is in unexpected state " + status);
        }
    }
}
