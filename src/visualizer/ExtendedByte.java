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

import java.util.Arrays;
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
		while ( hexString.length() >= 2)
    	{
    		    data.add(Byte.decode("#" + hexString.charAt(0) + hexString.charAt(1)));
    		    hexString = hexString.substring(2);
    	}
		while(data.size() < totalBytesNeeded)
			data.add(byteLiteral[0]);
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
	
	public byte[] convertBMPMetadata(LinkedList<ExtendedByte> input)
	{
		int size = convertFromBytes(input.get(1).getData()); 				//extended byte holding file size
		// System.out.println(size);
		ExtendedByte eb;
		byte[] temp = new byte[size];
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
	
	
	public static byte[] convertToBytes(int input)
	{
		String binary = Integer.toBinaryString(input);
		//System.out.println("convertToBytes() binary " + binary);
		byte[] temp = new byte[4];
		int zerosMissing = Integer.numberOfLeadingZeros(input);
		while(zerosMissing > 0)
		{
			binary = 0 + binary;
			zerosMissing--;
		}
		//System.out.println(binary);
		while(binary.length() < 32)
			binary = binary + 0;
		
		
		int i = 0;
		while(binary.length() > 0)
		{
				Integer intValue = Integer.parseInt(binary.substring(0,8), 2);
				temp[i] = intValue.byteValue();
				binary = binary.substring(8);
				i++;
		}
		//Now flip the array so that empty bytes are at the end
		byte[] tempFlipped = new byte[4];
		int head = 0; int index = 0;
		int tail = 3;
		
		while(index < temp.length)
		{
			if(temp[index] == byteLiteral[0])
			{
				tempFlipped[tail] = byteLiteral[0];
				index++;
				tail--;
			}
			else	
			{
				tempFlipped[head] = temp[index];
				index++;
				head++;
			}
		}
		return tempFlipped;
	}
	
	public static int convertFromBytes(byte[] input)
	{
		try
		{
			String s = "#" + new String(input);
			System.out.println("convertFromBytes: " +  s);
			return Integer.decode(s);
		}
		catch(NumberFormatException e)
		{
			
			for(byte b : input)
			{
				System.out.println(b);
			}
			//e.printStackTrace();
			return -777;
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(i < data.size()){
			sb.append(data.get(i) + " ");
			i++;
		}
		
		return "ArrayList Values: " + sb.toString();
		//return "Decimal Value: " + convertFromBytes(getData());
	}
	
	public void printArray(byte[] input)
	{
		System.out.println(Arrays.toString(input));
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

}
