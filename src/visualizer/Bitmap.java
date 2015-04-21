package visualizer;

import java.util.Arrays;

public class Bitmap implements SupportedType{
	public String fileName;
	public byte[] data;
	public byte[] pixelArray;
	
	public String 	headerField; 	//used to identify the file as a BMP or DIB file.
	public int 		fileSize;       //size of the bmp file in bytes
	public int 		reserved1;		//Reserved, value depends on application that creates image.
	public int 		reserved2;		//Reserved, see above
	public int		pixelArrayOffset;//starting address of the byte where the bitmap image dataRead is.
	
	

	public Bitmap(String fileName, byte[] data)
	{
		this.fileName = fileName;
		this.data = data;
		
		headerField = new String(data, 0, 2); //Constructor for string (byte array, offset, length)
		
		
		/**
		 * Takes the byte values from dataRead that are in little endian (least significant digit first)
		 * flips them, and removes the extra 0s from the front.
		 */
		String hexFileSize = Integer.toHexString(((data[5] << 24) | (data[4] << 16) | (data[3] << 8) | data[2]));
		while(hexFileSize.startsWith("f") && !hexFileSize.isEmpty())
		{
			hexFileSize = hexFileSize.replaceFirst("f","");
		}
		fileSize = Integer.decode("0x" + hexFileSize);
		
		pixelArray = Arrays.copyOfRange(data, pixelArrayOffset,data.length);
	}
	
	
	
	public String getMetaData()
	{
		StringBuilder stb = new StringBuilder();
		stb.append(fileName);
		stb.append("\n");
		stb.append("Size: " + fileSize);
		
		return stb.toString();
	}
	
	public void printHeaderData()
	{
		System.out.println(fileSize);
		//System.out.println();
	}
	
	public void printFileData()
	{
		
	}
	
}
