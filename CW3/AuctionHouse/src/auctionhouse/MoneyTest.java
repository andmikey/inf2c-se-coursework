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
    public void testAddWithRound() {
        Money val1 = new Money("12");
        Money val2 = new Money("0.005");
        Money result = val1.add(val2);
        assertEquals("12.01", result.toString());
    }
    
    @Test    
    public void testAddWithoutRound() {
        Money val1 = new Money("12");
        Money val2 = new Money("0.004");
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

    @Test    
    public void testSubWithRound() {
        Money val1 = new Money("12");
        Money val2 = new Money("0.005");
        Money result = val1.subtract(val2);
        assertEquals("11.99", result.toString());
    }
    
    @Test    
    public void testSubWithoutRound() {
        Money val1 = new Money("12");
        Money val2 = new Money("0.004");
        Money result = val1.subtract(val2);
        assertEquals("12.00", result.toString());
    }

    @Test
    public void addPercent() {
	Money val1 = new Money("10");
	double percent = 10.0;
	Money result = val1.addPercent(percent);
	assertEquals("11.00", result.toString());
    }

    @Test
    public void addZeroPercent() {
	Money val1 = new Money("10");
	double percent = 0.0;
	Money result = val1.addPercent(percent);
	assertEquals("10.00", result.toString());
    }
    
    @Test
    public void addNegativePercent() {
	Money val1 = new Money("10");
	double percent = -10.0;
	Money result = val1.addPercent(percent);
	assertEquals("9.00", result.toString());
    }
    
    @Test
    public void addHundredPercent() {
	Money val1 = new Money("10");
	double percent = 100.0;
	Money result = val1.addPercent(percent);
	assertEquals("20.00", result.toString());
    }

    @Test
    public void addVerySmallPercent() {
	Money val1 = new Money("10");
	double percent = 0.0005;
	Money result = val1.addPercent(percent);
	assertEquals("10.00", result.toString());
    }

    @Test
    public void toStringSimple() {
	String string = "10.00";
	Money val1 = new Money(string);
	String result = val1.toString();
	assertEquals(string, result);
    }
    
    @Test
    public void toStringWithNoDec() {
	String string = "10";
	Money val1 = new Money(string);
	String result = val1.toString();
	assertEquals("10.00", result);
    }
    
    @Test
    public void toStringWithSmallDec() {
	String string = "10.0005";
	Money val1 = new Money(string);
	String result = val1.toString();
	assertEquals("10.00", result);
    }

    @Test
    public void toStringWithRound() {
	String string = "10.005";
	Money val1 = new Money(string);
	String result = val1.toString();
	assertEquals("10.01", result);
    }

    @Test
    public void toStringWithoutRound() {
	String string = "10.004";
	Money val1 = new Money(string);
	String result = val1.toString();
	assertEquals("10.00", result);
    }
    
    /*
     * Put all class modifications above.
     ***********************************************************************
     * END MODIFICATION AREA
     ***********************************************************************
     */


}
