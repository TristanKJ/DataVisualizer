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

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class ExtendedByte {
	
	//private static final char NULL_CHARACTER = '\0';
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
	
	private int initialValue;
	private String initialString;
	
	
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
		initialValue = input;
		
		byte[] temp = toByteArray(input);
		for(byte b : temp)
		{
			if(b != byteLiteral[0])
				data.add(b);
		}
		while(data.size() < requiredByteLength)
			data.add(byteLiteral[0]);
		
		arrayListToLittleEndian();
	}
	
	/**
	 * Empty Bytes
	 * Creates Extended Byte of specified size
	 * Initialized to be zero.
	 * @param emptyBytesNeeded
	 */
	public ExtendedByte(int emptyBytesNeeded)
	{
		data = new LinkedList<Byte>();
		
		while(emptyBytesNeeded > 0)	
		{
			data.add(byteLiteral[0]);
			emptyBytesNeeded--;
		}
	}
	
	public ExtendedByte(String hexString, int totalBytesNeeded)
	{
		data = new LinkedList<Byte>();
		initialString = hexString + "";
		while ( hexString.length() >= 2)
    	{
    		    data.add(Byte.decode("#" + hexString.charAt(0) + hexString.charAt(1)));
    		    System.out.println("chars @ 0-1: " + hexString.charAt(0) + hexString.charAt(1));
    		    hexString = hexString.substring(2);
    	}
		while(data.size() < totalBytesNeeded)
			data.add(byteLiteral[0]);
		
		System.out.println(this);
		arrayListToLittleEndian();
	}
	
	public byte[] getData()
	{
		Byte[] temp = new Byte[data.size()];
		int index = 0;
		while(index < temp.length)
		{
			temp[index] = data.get(index);
			index++;
		}
		return unboxByteArray(temp);
	}
	
	public static byte[] convertBMPMetadata(LinkedList<ExtendedByte> input)
	{
		int size = fromLittleEndianByteArray (input.get(1).getData()); 	//extended byte holding file size
		byte[] temp = new byte[size];
		
		ExtendedByte eb;
		int index = 0;
		while(input.peek() != null)
		{
			eb = input.poll();
			while(eb.data.peek() != null)
			{
				temp[index] = eb.data.poll();
				index++;
			}
		}
		return temp;
	}
	
	public void arrayListToLittleEndian()
	{
		Iterator<Byte> it = data.iterator();
		int startingSize = data.size();
		while(it.hasNext())
		{
			Byte b = it.next();
			if(b.byteValue() == byteLiteral[0])
			{
				it.remove();
			}
		}
		while(data.size() < startingSize)
			data.add(byteLiteral[0]);

	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(i < data.size()){
			sb.append(data.get(i) + " ");
			i++;
		}
		printArray();
		return "Decimal Value: " + fromLittleEndianByteArray(getData());
	}
	
	public void printArray()
	{
		System.out.println("ArrayList Values: " + Arrays.toString(getData()));
	}
	
	
	/**
	 * Because Java apparently isn't smart enough to figure this out.
	 * @param input
	 * @return A byte[] thats equivalent to the input
	 */
	private byte[] unboxByteArray(Byte[] input)
	{
		byte[] temp = new byte[input.length];
		for(int i = 0; i < input.length; i++)
		{
			temp[i] = input[i].byteValue();
		}
		return temp;
	}
	
	
	
	
	
	
	public static byte[] toByteArray(int value) {
	     return  ByteBuffer.allocate(4).putInt(value).array();
	}
/**
	public static byte[] toLittleEndianByteArray(int value) { //untested Method
	    return new byte[] {
	        (byte) (value >> 8),
	        (byte) (value >> 16),
	        (byte) (value >> 24),
	        (byte) value};
	}
	**/

	public static int fromByteArray(byte[] bytes) {
		if(bytes.length < 4)
		{
			byte[] temp = new byte[4];
			Arrays.fill(temp, byteLiteral[0]);
			for(int i = 3; i < bytes.length; i--)
			{
				temp[i] = bytes[i];
			}
			bytes = temp;
		}
	     return ByteBuffer.wrap(bytes).getInt();
	}

	public static int fromLittleEndianByteArray(byte[] bytes) {
		if(bytes.length < 4)
		{
			byte[] temp = new byte[4];
			Arrays.fill(temp, byteLiteral[0]);
			for(int i = 3; i < bytes.length; i--)
			{
				temp[i] = bytes[i];
			}
			bytes = temp;
		}
	     return bytes[3] << 24 | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
	}
	
	
	

}
