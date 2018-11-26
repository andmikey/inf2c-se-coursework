/**
 * 
 */
package auctionhouse;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author pbj
 *
 */
public class AuctionHouseTest {

    private static final double BUYER_PREMIUM = 10.0;
    private static final double COMMISSION = 15.0;
    private static final Money INCREMENT = new Money("10.00");
    private static final String HOUSE_ACCOUNT = "AH A/C";
    private static final String HOUSE_AUTH_CODE = "AH-auth";

    private AuctionHouse house;
    private MockMessagingService messagingService;
    private MockBankingService bankingService;

    /*
     * Utility methods to help shorten test text.
     */
    private static void assertOK(Status status) { 
        assertEquals(Status.Kind.OK, status.kind);
    }
    private static void assertError(Status status) { 
        assertEquals(Status.Kind.ERROR, status.kind);
    }
    private static void assertSale(Status status) { 
        assertEquals(Status.Kind.SALE, status.kind);
    }
    private static void assertNoSale(Status status) { 
        assertEquals(Status.Kind.NO_SALE, status.kind);
    }
    
    /*
     * Logging functionality
     */

    // Convenience field.  Saves on getLogger() calls when logger object needed.
    private static Logger logger;

    // Update this field to limit logging.
    public static Level loggingLevel = Level.ALL;

    private static final String LS = System.lineSeparator();

    @BeforeClass
    public static void setupLogger() {

        logger = Logger.getLogger("auctionhouse"); 
        logger.setLevel(loggingLevel);

        // Ensure the root handler passes on all messages at loggingLevel and above (i.e. more severe)
        Logger rootLogger = Logger.getLogger("");
        Handler handler = rootLogger.getHandlers()[0];
        handler.setLevel(loggingLevel);
    }

    private String makeBanner(String testCaseName) {
        return  LS 
                + "#############################################################" + LS
                + "TESTCASE: " + testCaseName + LS
                + "#############################################################";
    }

    @Before
    public void setup() {
        messagingService = new MockMessagingService();
        bankingService = new MockBankingService();

        house = new AuctionHouseImp(
                    new Parameters(
                        BUYER_PREMIUM,
                        COMMISSION,
                        INCREMENT,
                        HOUSE_ACCOUNT,
                        HOUSE_AUTH_CODE,
                        messagingService,
                        bankingService));

    }
    /*
     * Setup story running through all the test cases.
     * 
     * Story end point is made controllable so that tests can check 
     * story prefixes and branch off in different ways. 
     */
    private void runStory(int endPoint) {
        assertOK(house.registerSeller("SellerY", "@SellerY", "SY A/C"));       
        assertOK(house.registerSeller("SellerZ", "@SellerZ", "SZ A/C")); 
        if (endPoint == 1) return;
        
        assertOK(house.addLot("SellerY", 2, "Painting", new Money("200.00")));
        assertOK(house.addLot("SellerY", 1, "Bicycle", new Money("80.00")));
        assertOK(house.addLot("SellerZ", 5, "Table", new Money("100.00")));
        if (endPoint == 2) return;
        
        assertOK(house.registerBuyer("BuyerA", "@BuyerA", "BA A/C", "BA-auth"));       
        assertOK(house.registerBuyer("BuyerB", "@BuyerB", "BB A/C", "BB-auth"));
        assertOK(house.registerBuyer("BuyerC", "@BuyerC", "BC A/C", "BC-auth"));
        if (endPoint == 3) return;
        
        assertOK(house.noteInterest("BuyerA", 1));
        assertOK(house.noteInterest("BuyerA", 5));
        assertOK(house.noteInterest("BuyerB", 1));
        assertOK(house.noteInterest("BuyerB", 2));
        if (endPoint == 4) return;
        
        assertOK(house.openAuction("Auctioneer1", "@Auctioneer1", 1));

        messagingService.expectAuctionOpened("@BuyerA", 1);
        messagingService.expectAuctionOpened("@BuyerB", 1);
        messagingService.expectAuctionOpened("@SellerY", 1);
        messagingService.verify(); 
        if (endPoint == 5) return;
        
        Money m70 = new Money("70.00");
        assertOK(house.makeBid("BuyerA", 1, m70));
        
        messagingService.expectBidReceived("@BuyerB", 1, m70);
        messagingService.expectBidReceived("@Auctioneer1", 1, m70);
        messagingService.expectBidReceived("@SellerY", 1, m70);
        messagingService.verify();
        if (endPoint == 6) return;
        
        Money m100 = new Money("100.00");
        assertOK(house.makeBid("BuyerB", 1, m100));

        messagingService.expectBidReceived("@BuyerA", 1, m100);
        messagingService.expectBidReceived("@Auctioneer1", 1, m100);
        messagingService.expectBidReceived("@SellerY", 1, m100);
        messagingService.verify();
        if (endPoint == 7) return;
        
        assertSale(house.closeAuction("Auctioneer1",  1));
        messagingService.expectLotSold("@BuyerA", 1);
        messagingService.expectLotSold("@BuyerB", 1);
        messagingService.expectLotSold("@SellerY", 1);
        messagingService.verify();       
        if (endPoint == 8) return;
        
        bankingService.expectTransfer("BB A/C",  "BB-auth",  "AH A/C", new Money("110.00"));
        bankingService.expectTransfer("AH A/C",  "AH-auth",  "SY A/C", new Money("85.00"));
        bankingService.verify();
        if (endPoint == 9) return;
        
    }
    
    @Test
    public void testEmptyCatalogue() {
        logger.info(makeBanner("emptyLotStore"));

        List<CatalogueEntry> expectedCatalogue = new ArrayList<CatalogueEntry>();
        List<CatalogueEntry> actualCatalogue = house.viewCatalogue();

        assertEquals(expectedCatalogue, actualCatalogue);

    }

    @Test
    public void testRegisterSeller() {
        logger.info(makeBanner("testRegisterSeller"));
        runStory(1);
    }

    @Test
    public void testRegisterSellerDuplicateNames() {
        logger.info(makeBanner("testRegisterSellerDuplicateNames"));
        runStory(1);     
        assertError(house.registerSeller("SellerY", "@SellerZ", "SZ A/C"));       
    }

    @Test
    public void testAddLot() {
        logger.info(makeBanner("testAddLot"));
        runStory(2);
    }
    
    @Test
    public void testViewCatalogue() {
        logger.info(makeBanner("testViewCatalogue"));
        runStory(2);
        
        List<CatalogueEntry> expectedCatalogue = new ArrayList<CatalogueEntry>();
        expectedCatalogue.add(new CatalogueEntry(1, "Bicycle", LotStatus.UNSOLD)); 
        expectedCatalogue.add(new CatalogueEntry(2, "Painting", LotStatus.UNSOLD));
        expectedCatalogue.add(new CatalogueEntry(5, "Table", LotStatus.UNSOLD));

        List<CatalogueEntry> actualCatalogue = house.viewCatalogue();

        assertEquals(expectedCatalogue, actualCatalogue);
    }

    @Test
    public void testRegisterBuyer() {
        logger.info(makeBanner("testRegisterBuyer"));
        runStory(3);       
    }

    @Test
    public void testNoteInterest() {
        logger.info(makeBanner("testNoteInterest"));
        runStory(4);
    }
      
    @Test
    public void testOpenAuction() {
        logger.info(makeBanner("testOpenAuction"));
        runStory(5);       
    }
      
    @Test
    public void testMakeBid() {
        logger.info(makeBanner("testMakeBid"));
        runStory(7);
    }
  
    @Test
    public void testCloseAuctionWithSale() {
        logger.info(makeBanner("testCloseAuctionWithSale"));
        runStory(8);
    }


    /* 
     * registerSeller
     */    
    @Test
    public void testAddSellerSimple() {
        logger.info(makeBanner("Adding a seller where no sellers exist should pass"));

        Status res = house.registerSeller("Seller", "@SellerAddr", "SellerAcc");
        assertOK(res);
    }

    @Test
    public void testAddSellerWithDuplicateAddress() {
        logger.info(makeBanner("Adding a seller where another seller with the same " +
                               "address exists should fail"));

        assertOK(house.registerSeller("Seller", "@SellerAddr", "SellerAcc"));
        assertError(house.registerSeller("Seller2", "@SellerAddr", "SellerAcc"));
    }

    @Test
    public void testAddSellerWithNullName() {
        logger.info(makeBanner("Adding a seller with a null name should fail"));

        assertError(house.registerSeller(null, "@SellerAddr", "SellerAcc"));
    }
    
    @Test
    public void testAddSellerWithNullAddress() {
        logger.info(makeBanner("Adding a seller with a null address should fail"));

        assertError(house.registerSeller("Seller", null, "SellerAcc"));
    }

    @Test
    public void testAddSellerWithNullBankAcc() {
        logger.info(makeBanner("Adding a seller with a null bank account should fail"));

        assertError(house.registerSeller("Seller", "@SellerAddr", null));
    }

    /* 
     * registerBuyer
     */    
    @Test
    public void testAddBuyerSimple() {
        logger.info(makeBanner("Adding a buyer where no buyers exist should pass"));

        Status res = house.registerBuyer("Buyer", "@BuyerAddr", "BuyerAcc", "BuyerAuth");
        assertOK(res);
    }

    @Test
    public void testAddBuyerWithDuplicateAddress() {
        logger.info(makeBanner("Adding a buyer where a buyer with the same " +
                               "address exists should fail"));

        assertOK(house.registerBuyer("Buyer", "@BuyerAddr", "BuyerAcc", "BuyerAuth"));
        assertError(house.registerBuyer("Buyer2", "@BuyerAddr", "BuyerAcc", "BuyerAuth"));
    }

    @Test
    public void testAddBuyerWithDuplicateName() {
        logger.info(makeBanner("Adding a buyer where a buyer with the same " +
                               "name exists should fail"));

        assertOK(house.registerBuyer("Buyer", "@BuyerAddr", "BuyerAcc", "BuyerAuth"));
        assertError(house.registerBuyer("Buyer", "@Buyer2Addr", "BuyerAcc", "BuyerAuth"));
    }

    @Test
    public void testAddBuyerWithNullName() {
        logger.info(makeBanner("Adding a buyer with a null name should fail"));

        assertError(house.registerBuyer(null, "@BuyerAddr", "BuyerAcc", "BuyerAuth"));
    }
    
    @Test
    public void testAddBuyerWithNullAddress() {
        logger.info(makeBanner("Adding a buyer with a null address should fail"));

        assertError(house.registerBuyer("Buyer", null, "BuyerAcc", "BuyerAuth"));
    }

    @Test
    public void testAddBuyerWithNullBankAcc() {
        logger.info(makeBanner("Adding a buyer with a null bank account should fail"));

        assertError(house.registerBuyer("Buyer", "@BuyerAddr", null, "BuyerAuth"));
    }
    
    @Test
    public void testAddBuyerWithNullBankAuth() {
        logger.info(makeBanner("Adding a buyer with a null bank authorisation should fail"));

        assertError(house.registerBuyer("Buyer", "@BuyerAddr", "BuyerAcc", null));
    }

    /* 
     * addLot
     */
    
    @Test
    public void testSimpleAddLot() {
        logger.info(makeBanner("Adding a lot where no lots exist should pass"));
        runStory(1);

        assertOK(house.addLot("SellerY", 1, "Foo", new Money("0.0")));
    }
    
    @Test
    public void testAddLotDuplicate() {
        logger.info(makeBanner("Adding a lot with a duplicate number should fail"));
        runStory(1);

        assertOK(house.addLot("SellerY", 1, "Foo", new Money("0.0")));
        assertError(house.addLot("SellerZ", 1, "Bar", new Money("0.0")));
    }

    @Test
    public void testAddLotSellerNotExists() {
        logger.info(makeBanner("Adding a lot with a non-existent seller should fail"));
        runStory(1);

        assertError(house.addLot("SellerX", 1, "Foo", new Money("0.0")));
    }

    @Test
    public void testAddLotNegativeReservePrice() {
        logger.info(makeBanner("Adding a lot with a negative reserve price should fail"));
        runStory(1);

        assertError(house.addLot("SellerY", 1, "Foo", new Money("-1.0")));
    }

    @Test
    public void testAddLotNullSeller() {
        logger.info(makeBanner("Adding a lot with a null seller should fail"));
        runStory(1);

        assertError(house.addLot(null, 1, "Foo", new Money("1.0")));
    }

    @Test
    public void testAddLotNullDescription() {
        logger.info(makeBanner("Adding a lot with a null description should fail"));
        runStory(1);

        assertError(house.addLot("SellerY", 1, null, new Money("1.0")));
    }

    @Test
    public void testAddLotNullPrice() {
        logger.info(makeBanner("Adding a lot with a null price should fail"));
        runStory(1);

        assertError(house.addLot("SellerY", 1, "Foo", null));
    }

    /* 
     * noteInterest
     */    
    @Test
    public void testNoteInterestTwice() {
        logger.info(makeBanner("Noting interest in a lot twice should fail"));
        runStory(4);
        assertError(house.noteInterest("BuyerA", 1));
    }

    @Test
    public void testNoteInterestWhileSold() {
        logger.info(makeBanner("Noting interest in a sold lot should fail"));
        runStory(8);
        assertError(house.noteInterest("BuyerC", 1));
    }

    @Test
    public void testNoteInterestBuyerNotExists() {
        logger.info(makeBanner("Non-existent buyer noting interest in a lot should fail"));
        runStory(4);
        assertError(house.noteInterest("BuyerD", 1));
    }
    
    @Test
    public void testNoteInterestLotNotExists() {
        logger.info(makeBanner("Buyer noting interest in a non-existent lot should fail"));
        runStory(4);
        assertError(house.noteInterest("BuyerA", 199));
    }

    /* 
     * openAuction
     */
    
    @Test
    public void testOpenAuctionAlreadyOpen() {
        logger.info(makeBanner("Auctioneer trying to open an already-open auction should fail"));
        runStory(5);
        assertError(house.openAuction("Autioneer2", "@Auctioneer2", 1));
    }

    @Test
    public void testOpenParallelAuction() {
        logger.info(makeBanner("Active auctioneer trying to open a second auction should fail"));
        runStory(5);
        assertError(house.openAuction("Auctioneer1", "@Auctioneer1", 5));
    }

    @Test
    public void testOpenNotFoundAuction() {
        logger.info(makeBanner("Trying to open an auction for a lot that doesn't exist should fail"));
        runStory(5);
        assertError(house.openAuction("Auctioneer1", "@Auctioneer1", 40));
    }

    @Test
    public void testAuctioneerOpensMultipleAuctions() {
        logger.info(makeBanner("An auctioneer opening an auction after closing one should pass"));
        runStory(9);
        assertOK(house.openAuction("Auctioneer1", "@Auctioneer1", 5));
    }

    @Test
    public void testOpenAuctionWithNoInterestedBuyers() {
        logger.info(makeBanner("Opening an auction with no interested buyers should fail"));
        runStory(3);
        assertError(house.openAuction("Auctioneer1", "@Auctioneer1", 1));
    }

    /* 
     * makeBid
     */
    
    @Test
    public void makeNegativeBid() {
        logger.info("Making a negative bid on a lot should fail");
        runStory(5);
        assertError(house.makeBid("BuyerA", 1, new Money("-1")));
    }
    
    @Test
    public void makeBidWhileNotInterested() {
        logger.info("Buyer bidding on a lot they are not interested in should fail");
        runStory(5);
        assertError(house.makeBid("BuyerC", 1, new Money("70")));
    }

    @Test
    public void makeBidBelowCurrentPrice() {
        logger.info("Placing a bid below the current price should fail");
        runStory(6);
        assertError(house.makeBid("BuyerB", 1, new Money("60.00")));
    }

    @Test
    public void makeBidBelowIncrement() {
        logger.info("Placing a bid below the increment should fail");
        runStory(6);
        assertError(house.makeBid("BuyerB", 1, new Money("71.00")));
    }

    /* 
     * closeAuction
     */
    
    @Test
    public void testCloseAuctionWithDifferentAuctioneer() {
        logger.info("Different auctioneer closing an auction than opening it should fail");
        runStory(7);
        assertError(house.closeAuction("Auctioneer2", 1)); 
    }

    @Test
    public void testCloseAuctionBelowReserve() {
        logger.info("Closing auction which is below reserve price should return NO_SALE");
        runStory(6);
        assertNoSale(house.closeAuction("Auctioneer1", 1));
    }

    @Test
    public void testCloseAuctionWithNoBids() {
        logger.info("Closing auction with no bids should return NO_SALE");
        runStory(5);
        assertNoSale(house.closeAuction("Auctioneer1", 1));
    }

    @Test
    public void testCloseClosedAuction() {
        logger.info("Closing a closed auction should fail");
        runStory(9);
        assertError(house.closeAuction("Auctioneer1", 1));
    }
}
