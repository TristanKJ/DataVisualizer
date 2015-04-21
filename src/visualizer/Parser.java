package visualizer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Parser {
    private static final int BMP_HEADER_SIZE = 14;
    private static final int DIB_HEADER_SIZE = 40;
    private static final int BYTES_PER_PIXEL = 3;
    private static final char NULL_CHARACTER = '\0';

	Path path;
	byte[] dataRead;
    byte[] modifiedData;
	
	public Parser()
	{
		//System.out.println(System.getProperty("user.dir"));
	}
	
	public void readBmpFile()
	{
		try{
			//path = Paths.get("testData/blue.png");
			path = Paths.get("testData/test1.bmp");
			dataRead = Files.readAllBytes(path);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

    public static byte[] StringToBytesASCII(String str)
    {
    	byte[] b;
    	if((str.length()%2) == 0){
            b = new byte[str.length()];
    	}
    	else
    	{
    		b = new byte[str.length() + 1 ];
    		b[str.length() + 1] = NULL_CHARACTER;
    	}
        for(int i = 0; i < str.length(); i++)
        {
            b[i] = (byte)str.charAt(i);
        }
        return b;
    }
    
    


    public void createBmpByteArrayFromText(TextFile text)
    {
        byte[] textBytes = text.getTextData();

        int modifiedLength = textBytes.length + BMP_HEADER_SIZE + DIB_HEADER_SIZE;
        modifiedData = new byte[modifiedLength]; //generate new array to have space for BMP/DIB metadata
        

        //Generate BMP Header metadata
        StringBuilder sb = new StringBuilder();
        LinkedList<String> metaData = new LinkedList<String>();
        //Each node of the list will represent a hexPair
        

        metaData.add("42");									   //magic number for BMP files
        metaData.add("4D");
        metaData.addAll(transformAndPadHex(modifiedLength, 4));//string representing length of new file in hex
        metaData.addAll(emptyBytes(2));		//unused field
        metaData.addAll(emptyBytes(2));		//unused field
        metaData.addAll(transformAndPadHex(BMP_HEADER_SIZE + DIB_HEADER_SIZE, 4)); //offset of the pixel array
        
        
        //Generate DIBHeaderMetadata
        metaData.addAll(transformAndPadHex(DIB_HEADER_SIZE, 4)); //DIB header size
        //Calculate the size of the image
        double totalPixels = textBytes.length / BYTES_PER_PIXEL;
        int imageSize = (int) Math.sqrt(totalPixels); //find dimensions of image, rounding down
        int pixelsLost = (int) (totalPixels - (imageSize * imageSize));
        
        
        metaData.addAll(transformAndPadHex(imageSize, 4)); //width of the image
        metaData.addAll(transformAndPadHex(imageSize, 4)); //height of the image
        
        metaData.addAll(transformAndPadHex(1,  2)); //color Planes Used
        metaData.addAll(transformAndPadHex((BYTES_PER_PIXEL * 8 ), 2)); //bits per pixel
        
        metaData.add(textBytes.length + ""); //size of Raw bitmap data.
        
        metaData.addAll(transformAndPadHex(2835, 4));
        metaData.addAll(transformAndPadHex(2835, 4));
        metaData.addAll(emptyBytes(4)); // number of colors in pallet
        metaData.addAll(emptyBytes(4)); // 0 means all colors are important
        
        
       // modifiedData = parseLinkedList(metaData);
        
        byte[] temp = parseLinkedList(metaData);
        modifiedData = new byte[temp.length + textBytes.length];
        
        int index = 0;
        while(index < temp.length)
        {
        	modifiedData[index] = temp[index];
        	index++;
        }
        int index2 = 0;
        while(index2 < textBytes.length)
        {
        	modifiedData[index] = textBytes[index2];
        	index++;
        	index2++;
        }     
    }

    /**
     * Add zeros to hex value strings to comply with the endianness.
     * hexString.length is divided by 2 to comply with the number of hex pairs needed.
     * @param hexString
     * @param requiredHexLength
     * @return String of appropriate length representing the hex value
     * 
     * Unused Method???
     */
    public String padHex(String hexString, int requiredHexLength)
    {
    	if(hexString.length() == 1)
    		hexString += "NULL";
        while((hexString.length() * 2) < requiredHexLength)
        {
            hexString = hexString + "NULL";
        }
        System.out.println("padhex: " + hexString);

        return hexString;
    }

    
    /**
     * Generates byte string using String Builder.
     * probably inefficient for strings smaller than 4.
     * @param numberOfBytes as hex
     * @return '00' for each byte requested
     */
    public LinkedList<String> emptyBytes(int bytesNeeded)
    {
    	LinkedList<String> temp = new LinkedList<String>();
    	while(bytesNeeded > 0)
    	{
    		temp.add("NULLNULL");
    		bytesNeeded--;
    	}
    	return temp;
    }
    
    /**
     * Takes an integer value, turns it into a hex value, and pads it to the right length.
     * @param input
     * @param requiredHexLength
     * @return
     */
    public LinkedList<String> transformAndPadHex(int input, int requiredHexLength)
    {
    	LinkedList<String> temp = new LinkedList<String>();
    	
    	String hexString = Integer.toHexString(input);
    	if(hexString.length() == 1)
        	hexString = "NULL" + hexString;
    	
        temp.add(hexString);
        
        while( temp.size() < requiredHexLength )
        {
        	temp.add("NULL");
        }
        
        return temp;
    }
     
    public byte[] parseLinkedList(LinkedList<String> list)
    {
    	byte[] temp = new byte[list.size() * 2];
    	int index = 0;
    	while(list.peek() != null)
    	{
    		String s = list.poll();
    		if(s.contains("NULL"))
    			temp[index] = (byte) 0b00000000;
    		else
    			temp[index] = Byte.decode("#" + s);
    		
    		index++;
    	}
    	return temp;
    }
    
    
    

    public void write(byte[] input, String outputFileName){
        //writing binary file.
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(outputFileName));
                output.write(input);
            }
            finally {
                output.close();
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
	
	
	
	public void printFileData()
	{
		for(byte b : dataRead)
		{
			System.out.println(b);
		}
	}
	
	public byte[] getDataRead()
	{
		byte[] temp = dataRead.clone();
		return temp;
	}

	
	public static void main(String[] args) throws Exception
	{
		
		Parser par = new Parser();
		
		
		par.readBmpFile();
		
	    File file = new File("testData/pixelTest.txt");
	    //TextFile text = new TextFile(file);

		//par.createBmpByteArrayFromText(new TextFile(file));
		//par.write(par.modifiedData, "testImage4.bmp");
		
		System.out.println(par.transformAndPadHex(2835, 4));
		
		
		//String s = "" + Parser.NULL_CHARACTER + Parser.NULL_CHARACTER;
		//System.out.println();
		
		/**
		Bitmap bmp = new Bitmap("testData/test1.bmp", par.getDataRead());
		bmp.printHeaderData();
		try{
		ImageGUI g = new ImageGUI(bmp);
		}
		catch (Exception e)
		{
			System.exit(1);
		}**/
	}
	
}