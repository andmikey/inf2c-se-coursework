/**
 * 
 */
package auctionhouse;

/**
 * Money is the class used to represent currencies within the AuctionHouse 
 * context. 
 * Supported operations include:
 * <ul>
 * <li> Creation of an object by passing a string representing the currency value;
 * <li> Comparable operations (compareTo, lessEqual, equals)
 * between Money objects and in the case of equals, objects which can
 * be coerced to Money objects
 * </ul> Support for adding, subtracting Money objects
 * </ul> Support for adding a percentage to a Money object
 * <p>
 * @author Michael Andrejczuk
 * @author Dylan Thinnes
 */
public class Money implements Comparable<Money> {
    /**
     * Internal representation of the value of the object
     */
    private double value;

    /** 
     * Given a value in pounds, returns the value in pence,
     * rounding as necessary. 
     * 
     * @param pounds the value to be converted
     * @return the value in pounds converted to pence 
     */
    private static long getNearestPence(double pounds) {
        return Math.round(pounds * 100.0);
    }

    /**
     * Returns a value in pounds normalised to have only
     * two decimal points of precision. 
     * 
     * @param pounds the value to be normalised
     * @return the value in pounds rounded to two decimal places
     */
    private static double normalise(double pounds) {
        return getNearestPence(pounds)/100.0;
        
    }

    /**
     * Public constructor for Money class. Creates a new
     * instance of Money object with value set to be the 
     * double value of the pounds argument, normalised as needed.
     *
     * @param pounds the value of the Money object as a String
     * @return a Money object
     */ 
    public Money(String pounds) {
        value = normalise(Double.parseDouble(pounds));
    }
    
    /**
     * Private constructor for Money class. Creates a new
     * instance of Money object with value set to be the 
     * value of the pounds argument, without normalisation.
     *
     * @param pounds the value of the Money object
     * @return a Money object
     */     
    private Money(double pounds) {
        value = pounds;
    }
    
    /**
     * Adds together the value of two Money objects and returns 
     * a new Money object representing their sum.
     *
     * @param m the Money object whose value to add
     * @return a new Money object whose value is the sum of 
     * the values of the current Money object and the argument
     * passed.
     */         
    public Money add(Money m) {
        return new Money(value + m.value);
    }

    /**
     * Subtracts the value of two Money objects and returns 
     * a new Money object representing their difference.
     *
     * @param m the Money object whose value to subtract
     * @return a new Money object whose value is the difference of 
     * the values of the current Money object and the argument
     * passed.
     */         
    public Money subtract(Money m) {
        return new Money(value - m.value);
    }

    /**
     * Adds a given percentage on to the current value and returns
     * a new Money object. 
     * 
     * @param percent the percentage to add
     * @return a new Money object whose value is the value of the current
     * money object added to percent * value. 
     */
    public Money addPercent(double percent) {
        return new Money(normalise(value * (1 + percent/100.0)));
    }
    
    /**
     * Returns the string representation of the Money object: 
     * a float with two decimal places. 
     */     
    @Override
    public String toString() {
        return String.format("%.2f", value);
        
    }
    public int compareTo(Money m) {
        return Long.compare(getNearestPence(value),  getNearestPence(m.value)); 
    }
    
    public Boolean lessEqual(Money m) {
        return compareTo(m) <= 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money)) return false;
        Money oM = (Money) o;
        return compareTo(oM) == 0;       
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(getNearestPence(value));
    }
      

}
