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
public class Actor {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private String address;
    private AuctionHouse auctionhouse;

    public ArrayList<CatalogueEntry> viewCatalogue () {
        return null;
    }

    public Actor (String address, AuctionHouse auctionhouse) {
        this.address = address;
        this.auctionhouse = auctionhouse;
    }

    public String getAddress () {
        return this.address;
    }

}
