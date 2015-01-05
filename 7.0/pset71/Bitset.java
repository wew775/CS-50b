/** Bitset.java
 *
 *  Stores sets of small integers, in the range 0 .. maxSize-1, 
 *    for user-specified 'maxSize'
 *
 *  NOT related to java.util.Bitset!
 *  
 *  This class was modified for the purpose of solving pset71.
 *  
 *  **********************************************************
 *  **********************************************************
 *  ***************** The methods:	**************************
 *  *	
 *  *	 Cardinality(), isSubset(), readSet(String str),     *
 *  * 	 getByteArray(), setByteArray()...					 *
 *  * 	 are the newly added features, complying with the    *
 *  *	 homework specifications.							 *
 *  *														 *
 *  **********************************************************
 *  **********************************************************
 *  
 *  
 *  Last Modified April 6, 2012
 *  Comments added 4/19/11   JJ
 */

import java.util.*;

class Bitset
{
    private byte [] byteArray;     // the array of bytes (8-bit integers)
    private int maxSize;           // max # of set elements allowed
    
    /***
     *  No-arg constructor for the class, begins with a size of 0 and the
     *  byte array set to null.
     */
    public Bitset ()                // make an empty set of capacity zero.
    {
        maxSize = 0;
        setByteArray(null);
    }

    /**
     *  This constructor takes a size and creates a byte array that will accommodate
     *  that number of elements.  Since there could possible be up to 7 bits needed
     *  before we would have another complete byte, 7 is added to the requested size, 
     *  then when the size is divided by 8 for the number of bytes, the extra bits
     *  have been allowed for.
     *
     *  @param  size       The number of elements requested for the Bitset
     */
    public Bitset (int size) 
    {
        maxSize   = size;
        int nbyte = (size + 7) / 8;
        setByteArray(new byte [nbyte]);          // new array, all zeroes
    }

    /***
     *  This constructor is a "copy constructor", which takes an existing Bitset
     *  and makes a duplicate of it.  Since no elements are being modified the
     *  param Bitset can be copied as bytes rather than doing individual bits.
     *
     *  @param   setA    The existing Bitset to duplicate
     */
    public Bitset (Bitset setA)
    {
        maxSize   = setA.maxSize;
        int nbyte = setA.getByteArray().length;
        setByteArray(new byte [nbyte]);          // new array, all zeroes
        System.arraycopy (setA.getByteArray(), 0, 
                          getByteArray(), 0, setA.getByteArray().length);  
    } 

    /**
    *  Sets the bit at given offset from address byteArray to 1 
    *  i.e., adds element 'offset' to the set
    *
    * @param   n   The bit to be set
    */
    private void setBit (int n)
    {
	/*  Remember, we can find out which _byte_ we need using division:
	    e.g., 25 / 8 tells us that bit 25 is located in byte 3.
	    However, we then need to locate the _bit_ to modify and this uses
	    remainder:  25 % 8 leaves 1, so bit 25 is in location 1 in that byte
	    (NOTE: each byte indexes from 0!!)
	*/
        int whichByte = n / 8;
        int whichBit = n % 8;
        getByteArray()[whichByte] |= (1 << whichBit);
    }

    /**
     *  Returns true if the bit at given offset from address 
     * .. byteArray is set; else false. 
     *  i.e., determines whether element 'offset' is in the set
     *
     *  @param   n    The bit to check for inclusion in the set
     *  @return       True if it's present, false if not
     */
    private boolean getBit (int n)
    {
        int whichByte = n / 8;
        int whichBit = n % 8;
        return ( (getByteArray()[whichByte] & (1 << whichBit)) != 0 );
    }

    /**
     *  Clears the bit at given offset from address byteArray to 0 
     *  i.e., removes element 'offset' from the set, if present.
     *
     *  @param   n    The bit to be cleared (excluded) from the set
     */
     private void clearBit (int n)
    {
        int whichByte = n / 8;
        int whichBit = n % 8;
        getByteArray()[whichByte] &= ( (1 << whichBit)^255);
    }

    /**
     *  This method checks first that a Bitset has a byte array, and if so
     *  it clears, or zeroes out, the existing array.
     */
    public void clear()
    {
        if (getByteArray() == null)
            error ("clear: Can't clear a set that hasn't been constructed!");
        for (int i = 0; i < getByteArray().length; i++)
            getByteArray()[i] = 0;
    }

    /**
     *  Change existing set to capacity 'size'  (see comments for the constructor
     *  for an explanation of the sizing math).
     *
     *  @param    size    The requested size for the set
     */
    public void setSize (int size)
    {
        maxSize   = size;
        int nbyte = (size + 7) / 8;
        setByteArray(new byte [nbyte]);    // new array, all zeroes
    }

    /**
     *  Checks to see whether the parameter is currently a member of the set
     *
     *  @param   i    The value to test for inclusion
     *  @return       True if that value is included in the set, false if not
     */
    public boolean member (int i)
    {
        if (i >= maxSize) return false;
        return ( getBit(i) );
    }

    /**
     *  Checks to see whether the parameter is currently a member of the set
     *  This method is included for easier code readability in programs using
     *  the Bitset class.
     *
     *  @param   i    The value to test for inclusion
     *  @return       True if that value is included in the set, false if not
     */
    public boolean contains (int i)  // same as member(), reads well:
    {                                // e.g., mySet.contains(3);
        return member(i);
    }

    /**
     *  This method will ensure that the parameter is within the allowable values
     *  for the set, then will add it to the set.  If it was already present, it
     *  remains so.
     *
     *  @param   i    The element to add to the set
     */
    public void include (int i)
    {
        if (i >= maxSize)
            error ("include: " + i + "  is too large to fit inside the set");
        setBit (i);
    }

    /**
     *  This method removes the parameter element from the set, after ensuring 
     *  that it is within the allowable limits of the set elements.  If it was
     *  not previously present, the set remains unchanged.
     *
     *  @param   i    The value to remove from the set
     */
    public void exclude (int i)
    {
        if (i >= maxSize)
            error ("exclude: " + i + "  is too large be inside the set");
        clearBit (i);
    }

    /**
     *  Copies setA to receiver, without changing latter's capacity.  In other 
     *  words, when passed another Bitset, the values that it contains are copied
     *  into _this_ Bitset's byte array.  If the parameter set is larger than
     *  this set, an error message is printed and the program halts.
     *
     *  @param   setA    The set that will be copied to this one.
     *  @return          This Bitset containing the new values
     */
    Bitset getSet (Bitset setA)
    {
        if (getByteArray().length < setA.getByteArray().length)
            error ("getSet: source set larger than dest. set");
        clear();

        int nbyte = setA.getByteArray().length;
        for (int i = 0; i < nbyte; i++) // copy byteArray from arg.
            getByteArray()[i] = setA.getByteArray()[i];
        return this;                    // return receiver, updated
    }

    /**
     *  This method performs union on itself combined with setB, and produces
     *  a new Bitset containing the values from both sets.
     *
     *  @param   setB    The Bitset to add (union) with _this_ Bitset
     *  @return          A new Bitset containing all the elements of the combined sets
     */
    public Bitset union (Bitset setB)
    {
        Bitset temp = new Bitset (maxSize>setB.maxSize ? this : setB);

        int nbyte = Math.min (getByteArray().length, setB.getByteArray().length);
        for (int i = 0; i < nbyte; i++)
            temp.getByteArray()[i] = (byte) (getByteArray()[i] | setB.getByteArray()[i]);
        return temp;
    }

    /**
     *  This method performs the difference between _this_ Bitset and the 
     *  parameter Bitset - it determines the elements in this set that are
     *  NOT present in setB, and returns them as a new Bitset.
     *
     *  @param   setB    The Bitset to "subtract" from _this_ Bitset
     *  @return          A new Bitset containing the elements in this Bitset
     *                   that are NOT in the parameter Bitset
     */
    public Bitset difference (Bitset setB)
    {
        Bitset temp = new Bitset (this);
        int nbyte = Math.min (getByteArray().length, setB.getByteArray().length);
        for (int i = 0; i < nbyte; i++)
            temp.getByteArray()[i] = (byte)(getByteArray()[i] & (setB.getByteArray()[i] ^ 255));
        return temp;
    }

    /**
     *  This method performs intersection on itself and the parameter Bitset.  A new
     *  resulting Bitset is created which has the elements that are present in both
     *  Bitsets and is returned.
     *
     *  @param   setB   The Bitset to intersect with _this_ Bitset
     *  @return         A new Bitset containing only the elements that are common to both sets
     */
    public Bitset intersect (Bitset setB)
    {
        Bitset temp = new Bitset (Math.min (maxSize, setB.maxSize));
        int nbyte = Math.min (getByteArray().length, setB.getByteArray().length);
        for (int i = 0; i < nbyte; i++)
            temp.getByteArray()[i] = (byte)(getByteArray()[i] & setB.getByteArray()[i]);
        return temp;
    }

    /**
     *  This method does content comparison to determine equality of sets.  Sets
     *  of different lengths are allowed, and will result in true as long as the
     *  larger set has no elements above the maxSize of the smaller set.
     *
     *  @param   setB   The Bitset to test (compare) to _this_ Bitset
     *  @return         True if the same elements are set in both Bitsets, false if not
     */
    public boolean equals (Bitset setB)
    {
	/*  Begin by getting the length of the _shorter_ Bitset array...  */
        int nbyte = Math.min (getByteArray().length, setB.getByteArray().length);

	/*  Now we test both sets for that length of elements  */
        for (int i = 0; i < nbyte; i++)
        {
            if (getByteArray()[i] != setB.getByteArray()[i]) return false;
        }

	/*  If the sets are not of equal length, is _this_ set the larger one?  If so,
	    go through and look for other elements above the maxSize of the other set...  */
        if (getByteArray().length > nbyte)
        {
            for (int i = nbyte; i < getByteArray().length; i++)
            {
              if (getByteArray()[i] != 0) return false;
            }
        }

	/*  Otherwise, is the _other_ set the larger one?  If so, go through and 
	    look for other elements greater than the maxSize of _this_ Bitset  */
        if (setB.getByteArray().length > nbyte)
        {
            for (int i = nbyte; i < setB.getByteArray().length; i++)
            {
              if (setB.getByteArray()[i] != 0) return false;
            }
        }
        return true;
    }

    /**
     *  This method tests for an empty, or null, set, by walking through the
     *  byteArray and checking each byte for inclusions (which produce a value
     *  greater than 0).  If any are found, the Bitset is not a null set.
     *
     *  @return       True if this is a null (empty) set, false if it is not.
     */
    public boolean isNull ()
    {
        for (int i = 0; i < getByteArray().length; i++)
        {
            if (getByteArray()[i] != 0) return false;
        }
        return true;
    }

    /**
     *  Utility method to read a set in from standard input via a Scanner.
     *  Each element requested is checked to be between 0 and maxSize, and if
     *  it meets this requirement, it is included in the current byteArray.
     *
     *  @param   in    The Scanner to read from
     */
    public void readSet (Scanner in) 
    {  
        clear ();
        while (in.hasNextInt())
        {   
            int n = in.nextInt();
            if (n >= 0 && n < maxSize)  include (n);
        }
        in.next ();
    }

    /**
     *  This creates a String representation of a Bitset object in standard
     *  set notation:  { x  x  x  x }  Elements present are listed.
     *
     *  @return       A String representing the Bitset in printable format
     */
    public String toString() 
    {  
       String str = "{  ";
       for (int i = 0 ; i < maxSize; i++)
       {
             if ( member(i)) str += i + "  ";
       }
       return str + "}";
    }

    /**
     *  A method to print a custom error message to the user, then exit
     *
     *  @param   msg   The message to print to the user
     */
    private void error (String msg)
    {
        System.out.print (" " + msg);
        System.exit (1);
    }
    
    /**
     * This method checks for the number of elements in the "this" set, and returns it as an int value
     * 
     * @return sum
     */
    public int cardinality()
    {
    	if ( !isNull() )													
    	{
    		// this is how many bits are in each byte. This constant will keep us from using magic numbers.
	    	final int NUMBER_OF_BITS = 8;					
	    	// each set has a max size. We know the number of bytes needed to store values base on the division below.
	    	final int NUMBER_OF_BYTES = this.maxSize / NUMBER_OF_BITS;	
	    	// sum is a counter for number of elements in the set.
	    	int sum = 0;
	    	
	    	/* The for loops will traverse through every byte and count how many times a byte is set to TRUE */
	    	for (int i = 0; i < NUMBER_OF_BYTES; i++)
	    	{
	    		for (int j = 0; j < NUMBER_OF_BITS; j++)
	    		{
	    			/*
	    			 *  This says: if the jᵗʰ bit in the iᵗʰ byte (excluding all other bits)
	    			 *  is set to TRUE  	 --- (a & b) is only true when TRUE:TRUE or 1:1, and 0:0 or 0:1 are FALSE or = 0 ---
	    			 *  increment the sum by 1.
	    			 */
	    			if ( (byteArray[i] & (1 << j))  != 0 )
	    				sum++;
	    		}
	    	}
	    	return sum;
    	}
    	else 
    		return 0;
    }
    
    /**
     * This method returns true if the input parameter is a subset of this Bitset class. 
     * @param b
     * @return true if all elements in b are present in this object. Else, false.
     */
    public boolean isSubset(Bitset b)
    {
    	// if this Bitset is Null, it means it's empty.. If it's empty, it is a subset.
    	if ( isNull() )
    		return true;
    	
    	/*
    	 *  if this.byteArray is longer than b.byteArray, the extra byte must be 0 for it to be able to be a subset of the smaller array.
    	 *  E.g. ...if this.byteArray[0] = { 1, 2, 3 }, and b.byteArray[0] = {1, 2, 3, 4 } 
    	 *  this.byteArray could be a a subset of b.byteArray only if there is nothing at this.byteArray[1].
    	 *  If this.byteArray[1] has elements, 
    	 */
    	if (this.getByteArray().length > b.getByteArray().length)
    		if (this.getByteArray()[1] != 0)
    			return false;
    	
    	if ((((byteArray[0] ^ b.getByteArray()[0]) | b.getByteArray()[0]) ^ b.getByteArray()[0]) != 0)
    		return false;
    	
    	return true;
    }
    
    /**
     *  This method is a modification of readSet(Scanner in).
     *  It takes a String as input, and splits it into various elements using the space character as delimiter.
     *  Then, it checks whether the elements are between 0 and maxSize, and if
     *  it meets this requirement, the element is included in the current byteArray.
     *  
     *  Only TestSetGUI.java uses this method. The GUI does not use a scanner, so it must read strings.
     *
     *  @param   in    The Scanner to read from
     */
    public void readSet (String in) 
    {  
    	// clear all previous elements to start anew.
        clear ();
        // create new StringTokenizer object.
        StringTokenizer st = new StringTokenizer(in);
        // temporary storage for each non-spaced element in the string
        String token;
        // temporary storage for the int value of each token
        int n;
        
        while (st.hasMoreElements())
        {
        	token = st.nextElement().toString();
            n = Integer.parseInt(token);								// the try block is run in TestSetGUI
            if (n >= 0 && n < maxSize)  include (n);
        }
    }

	/**
	 * @return the byteArray
	 */
	public byte [] getByteArray() {
		return byteArray;
	}

	/**
	 * @param byteArray the byteArray to set
	 */
	public void setByteArray(byte [] byteArray) {
		this.byteArray = byteArray;
	}
}