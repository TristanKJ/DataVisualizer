/**
 * The purpose of this class is to give me more control over the way that
 * I am able to process and manipulate bytes. All Byte translation and
 * creation should be changed to use this class.
 * 
 * The methods currently used to generate the different LinkedLists of strings
 * will be implemented here as constructors.
 * @author RedDwarf
 *
 */

package visualizer;

import java.util.LinkedList;

public class ExtendedByte {
	
	private static final char NULL_CHARACTER = '\0';
	private static final Byte[] byteLiteral = //byte literals for digits 0-9
    	{0b00000000,
    	 0b00000001,
    	 0b00000010,
    	 0b00000011,
    	 0b00000100,
    	 0b00000101,
    	 0b00000110,
    	 0b00000111,
    	 0b00001000,
    	 0b00001001 };
	
	public LinkedList<Byte> data;
	
	
	public ExtendedByte()
	{
		data = new LinkedList<Byte>();
	}
	
	
	
	/**
	 * Initializes extended byte with value of input
	 * and adds trailing zeros to match the requested Length
	 * @param input
	 * @param requiredByteLength
	 */
	public ExtendedByte(int input, int requiredByteLength)
	{
		data = new LinkedList<Byte>();
		
		byte[] temp = convertToBytes(input);
		for(byte b : temp)
		{
			data.add(b);
		}
		while(data.size() < requiredByteLength)
			data.add(byteLiteral[0]);
	}
	
	/**
	 * Empty Bytes
	 * Creates Extended Byte of specified size
	 * Initialized to be zero.
	 * @param bytesNeeded
	 */
	public ExtendedByte(int bytesNeeded)
	{
		data = new LinkedList<Byte>();
		
		while(bytesNeeded > 0)	
		{
			data.add(byteLiteral[0]);
			bytesNeeded--;
		}
	}
	

	public Byte[] getData()
	{
		Byte[] temp = data.toArray(new Byte[0]);
		return temp;
	}
	
	
	public static byte[] convertToBytes(int input)
	{
		String hexString = Integer.toHexString(input);
		return hexString.getBytes();
		
	}

}
