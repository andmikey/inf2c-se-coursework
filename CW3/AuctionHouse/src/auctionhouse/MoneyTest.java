/**
 * 
 */
package auctionhouse;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author pbj
 *
 */
public class MoneyTest {

    @Test    
    public void testAdd() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.66");
        Money result = val1.add(val2);
        assertEquals("13.00", result.toString());
    }

    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * Add all your JUnit tests for the Money class below.
     */
    @Test    
    public void testAddNegative() {
        Money val1 = new Money("12");
        Money val2 = new Money("-1");
        Money result = val1.add(val2);
        assertEquals("11.00", result.toString());
    }

    @Test    
    public void testAddZero() {
        Money val1 = new Money("12");
        Money val2 = new Money("0");
        Money result = val1.add(val2);
        assertEquals("12.00", result.toString());
    }

    @Test    
    public void testAddVerySmallNumber() {
        Money val1 = new Money("12");
        Money val2 = new Money("0.00005");
        Money result = val1.add(val2);
        assertEquals("12.00", result.toString());
    }

    @Test    
    public void testSub() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.34");
        Money result = val1.subtract(val2);
        assertEquals("12.00", result.toString());
    }

    @Test    
    public void testSubNegative() {
        Money val1 = new Money("12");
        Money val2 = new Money("-1");
        Money result = val1.subtract(val2);
        assertEquals("13.00", result.toString());
    }

    @Test    
    public void testSubZero() {
        Money val1 = new Money("12");
        Money val2 = new Money("0");
        Money result = val1.subtract(val2);
        assertEquals("12.00", result.toString());
    }

    @Test    
    public void testSubVerySmallNumber() {
        Money val1 = new Money("12");
        Money val2 = new Money("0.00005");
        Money result = val1.subtract(val2);
        assertEquals("12.00", result.toString());
    }
    /*
     * Put all class modifications above.
     ***********************************************************************
     * END MODIFICATION AREA
     ***********************************************************************
     */


}
