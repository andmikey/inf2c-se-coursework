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
    private ArrayList<Auctioneer> Auctioneers;

    //Commented out until we have a Actor superclass
    //private HashMap<String, Actor> addressBook;
    private ArrayList<CatalogueEntry> catalogue;
   
    public AuctionHouseImp(Parameters parameters) {

    }

    public Status registerBuyer(
            String name,
            String address,
            String bankAccount,
            String bankAuthCode) {
        logger.fine(startBanner("registerBuyer " + name));
        
        return Status.OK();
    }

    public Status registerSeller(
            String name,
            String address,
            String bankAccount) {
        logger.fine(startBanner("registerSeller " + name));
        
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

	// Find the seller
	// TODO add this as separate method to also apply to buyers / auctioneers
	boolean foundSeller = false;
	Seller assocSeller = null;

	for (Seller seller : this.sellers) {
	    if (seller.name == sellerName) {
		foundSeller = true;
		assocSeller = seller;
		break;
	    }
	}

	if (!foundSeller) {
	    return Status.error("Cannot find seller of name " + sellerName +
				", so lot cannot be added");
	}
	
	Lot newLot = new Lot(assocSeller, number, description, reservePrice);
	CatalogueEntry catEntry = new CatalogueEntry(number, description, LotStatus.UNSOLD);
	
        return Status.OK();    
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
        
        return Status.OK();
    }

    public Status makeBid(
            String buyerName,
            int lotNumber,
            Money bid) {
        logger.fine(startBanner("makeBid " + buyerName + " " + lotNumber + " " + bid));

        return Status.OK();    
    }

    public Status closeAuction(
            String auctioneerName,
            int lotNumber) {
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));
 
        return Status.OK();  
    }
}
