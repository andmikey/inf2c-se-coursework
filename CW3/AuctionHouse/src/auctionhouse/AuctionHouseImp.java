/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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
        
	    if (sellerName == null) {
	        return Status.error("Cannot add a lot without a seller");
	    }
	    else if (description == null) {
	        return Status.error("Cannot add a lot without a description");
	    }
	    else if (reservePrice == null) {
	        return Status.error("Cannot add a lot without a reserve price");
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

        // Note that by not associating an entry with a lot explicilty, we need
        // to make sure we update the catentry status when the lot status is
        // updated
        CatalogueEntry catEntry = new CatalogueEntry(number, description, LotStatus.UNSOLD);
	catalogue.add(catEntry);
        
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
        
        List<CatalogueEntry> catalogue = new ArrayList<CatalogueEntry>();
        logger.fine("Catalogue: " + catalogue.toString());
        return catalogue;
    }

    public Status noteInterest(
            String buyerName,
            int lotNumber) {
        logger.fine(startBanner("noteInterest " + buyerName + " " + lotNumber));
        
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

        // Make sure lot can be opened
        Status openingAttempt = lot.open();
        if (openingAttempt.kind != Status.Kind.ERROR) {
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

        Lot lot = this.lots.get(lotNumber);
        if (lot == null) {
            return Status.error("Lot with number " + lotNumber + " not found.");
        }

        return Status.OK();    
    }

    public Status closeAuction(
            String auctioneerName,
            int lotNumber) {
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));
 
        return Status.OK();  
    }
}
