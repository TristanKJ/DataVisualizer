package visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedByteTest {
	
	@Test
	public void testByteLength2Fields()
	{
		ExtendedByte eb = new ExtendedByte(1, 2);
		System.out.println(eb);
		assertEquals(2, eb.data.size());
	}
	
	@Test //Test that the same number that gets put in is what comes out.
	public void testHexToByteConversions() {
		for(int i = 1; i < 5000; i++)
		{
			assertEquals(i, ExtendedByte.fromByteArray(ExtendedByte.toByteArray(i)));
		}
	}
	
	@Test
	public void testStringConversionToHex()
	{
		ExtendedByte eb = new ExtendedByte("424D",2);
		byte[] byteLiteralOfInput = {0b01000010, 0b01001101};
		
		assertArrayEquals( byteLiteralOfInput, eb.getData());
	}
	
	@Test
	public void testDecimalToHexConversion()
	{
		ExtendedByte eb = new ExtendedByte("130B", 2);
		byte[] byteLiteralOfInput = {0b00001011, 0b00010011};
		//byte[] byteLiteralOfInput = {0b00001011, 0b00010011, 0b00000000, 0b00000000};

		System.out.println("2835: " + eb);
		assertArrayEquals(byteLiteralOfInput, eb.getData());
	}
	
	@Test
	public void testDecimalToHexConversion2()
	{
		ExtendedByte eb = new ExtendedByte(6461, 4);
		byte[] byteLiteralOfInput = {0b00011001, 0b00111101, 0b00000000, 0b00000000};
		
		
		System.out.println(eb.toString());
		
		assertArrayEquals(byteLiteralOfInput, eb.getData());
	}
	@Test
	public void testDecimalToHexConversion3()
	{
		ExtendedByte eb = new ExtendedByte(538984961, 4);
		byte[] byteLiteralOfInput = {0b00100000, 0b00100000, 0b01000010, 0b00000001};
		
		assertArrayEquals(byteLiteralOfInput, eb.getData());
	}

}
