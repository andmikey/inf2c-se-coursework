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

    //Commented out until we have a Actor superclass
    private HashMap<String, Actor> addressBook;
    private ArrayList<CatalogueEntry> catalogue;

    private Parameters parameters;
   
    public AuctionHouseImp(Parameters parameters) {
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
            return Status.error("Address " + address + " already belongs to an " +
                                "existing user, cannot register buyer");
        }
        
        logger.fine(baseMessage + "checking username is not duplicate");        
        Buyer existingBuyer = findBuyer(username);        
        if (existingBuyer != null) {
            return Status.error("Username " + username + " already belongs to an " +
                                "existing buyer, cannot register buyer");
        }

        logger.fine(baseMessage + "creating Buyer object");        
        Buyer buyer = new Buyer(username, address, this, bankAuthCode, bankAccount);

        logger.fine(baseMessage + "adding to relevant lists");        
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
            logger.fine(baseMessage + "address is duplicate, returning error");
            return Status.error("Address " + address + " already belongs to an " +
                                "existing user, cannot register seller");
        }

        logger.fine(baseMessage + "checking username is not duplicate");        
        Seller existingSeller = findSeller(username);        
        if (existingSeller != null) {
            return Status.error("Username " + username + " already belongs to an " +
                                "existing seller, cannot register seller");
        }

        logger.fine(baseMessage + "creating Seller object");        
        Seller seller = new Seller(username, address, this, null, bankAccount);

        logger.fine(baseMessage + "adding to relevant lists");        
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
            return Status.error("Reserve price of lot cannot be less than zero");
        }
        
	    // Check there's no catalogue entries with the same number
	    // Note that we can't use .equals as it compares number, desc, *and* status
	    // which would allow for two entries with the same number but different statuses
	    for (CatalogueEntry entry : catalogue) {
	        if (entry.lotNumber == number) {
	    	return Status.error("Cannot add a lot with the same number as " +
	    		     " an existing lot. Conflicting lot: \n " + entry.toString());
	        }
	    }

        Seller seller = findSeller(sellerName);

	    if (seller == null) {
	        return Status.error("Cannot find seller of username " + sellerName +
	    			", so lot cannot be added");
	    }
        
        Lot newLot = new Lot(seller, number, description, reservePrice);
        // Insert lot into our hashmap for future referencing
        this.lots.put(number, newLot);

        // Note that by associating an entry with a lot by passing it in, the Lot can keep the catalogue entry up to date by mutating it.
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
        logger.fine(startBanner("viewCatalog"));
        
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
        
        // Make sure lot exists
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            return Status.error("Lot with number " + lotNumber + " not found.");
        }

        // Make sure buyer exists
        Buyer buyer = findBuyer(buyerName);        
        if (buyer == null) {
            return Status.error("Buyer with username " + buyerName + " not found.");
        }

        Status lotAdd = lot.addInterestedBuyer(buyer);
        if (lotAdd.kind == Status.Kind.ERROR) {
            return lotAdd;
        }
        
        return Status.OK();   
    }

    public Status openAuction(
            String auctioneerName,
            String auctioneerAddress,
            int lotNumber) {
        logger.fine(startBanner("openAuction " + auctioneerName + " " + lotNumber));

        // Make sure lot exists
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            return Status.error("Lot with number " + lotNumber + " not found.");
        }

        Auctioneer auctioneer = this.findAuctioneer(auctioneerName);
        
        // Make sure lot can be opened
        Status openingAttempt = lot.open(auctioneer);
        if (openingAttempt.kind == Status.Kind.ERROR) {
            // If lot opening fails, return the error that it failed with
            return openingAttempt;
        }

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

        // Make sure lot exists
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            return Status.error("Lot with number " + lotNumber + " not found.");
        }
        
        // Bid is less than minimum increment
        // Note that this also handles negative numbers, so we don't need to check this
        if (bid.compareTo(this.parameters.increment) == -1) {
            return Status.error("Bid must be greater than minimum increment " +
                                this.parameters.increment.toString());
        }
        
        // TODO add messaging to auctioneer, interested buyers, seller
        
        return Status.OK();    
    }

    public Status closeAuction(
            String auctioneerName,
            int lotNumber) {
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));
        // Find lot
        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            return Status.error("Lot with number " + lotNumber + " not found.");
        }

        // Identify auctioneer
        
        // Call close on lot

        // Inform buyers, seller

        // Try to take payments

        // If payment failed, set lot status to sold pending payment
        
        return Status.OK();  
    }
}
