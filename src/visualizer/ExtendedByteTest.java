package visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedByteTest {

	//@Test //Test that the same number that gets put in is what comes out.
	public void testHexToByteConversions() {
		ExtendedByte eb = new ExtendedByte();
		for(int i = 0; i < 5000; i++)
		{
			assertEquals(i,eb.convertFromBytes(eb.convertToBytes(i)));
		}
	}
	
	//@Test
	public void testStringConversionToHex()
	{
		ExtendedByte eb = new ExtendedByte("424D",2);
		byte[] byteLiteralOfInput = {0b01000010, 0b01001101};
		
		assertArrayEquals( byteLiteralOfInput, eb.getData());
	}
	
	@Test
	public void testDecimalToHexConversion()
	{
		ExtendedByte eb = new ExtendedByte(2835, 4);
		byte[] byteLiteralOfInput = {0b00010011, 0b00001011, 0b00000000, 0b00000000};
		
		eb.printArray(byteLiteralOfInput);
		
		System.out.println("Output:");
		eb.printArray(eb.getData());
		System.out.println(eb);
		
		assertArrayEquals(byteLiteralOfInput, eb.getData());
	}
	
	//@Test
	public void testDecimalToHexConversion2()
	{
		ExtendedByte eb = new ExtendedByte(6589, 4);
		byte[] byteLiteralOfInput = {0b00011001, new Byte("-67").byteValue(), 0b00000000, 0b00000000};
		assertArrayEquals(byteLiteralOfInput, eb.getData());
	}
	//@Test
	public void testDecimalToHexConversion3()
	{
		ExtendedByte eb = new ExtendedByte(538984961, 4);
		byte[] byteLiteralOfInput = {0b00100000, 0b0010000, 0b001000, 0b000001};
		assertArrayEquals(byteLiteralOfInput, eb.getData());
	}

}
