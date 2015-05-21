package visualizer;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

public class Parser {
	Path path;
	byte[] textBytes;
	byte[] dataRead;
    byte[] modifiedData;
    LinkedList<ExtendedByte> metaData;
    ExtendedByte eb = new ExtendedByte();
	
	public Parser()
	{
		//System.out.println(System.getProperty("user.dir"));
	}
	
	public void readBmpFile(String filePath)
	{
		try{
			path = Paths.get(filePath);
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
    		b[str.length() + 1] = Constants.NULL_CHARACTER;
    	}
        for(int i = 0; i < str.length(); i++)
        {
            b[i] = (byte)str.charAt(i);
        }
        return b;
    }
    
    public void CreateBmpFileFromData(TextFile textToBeConverted)
    {
    	createBmpHeader(textToBeConverted);
    	createDibHeader();
    	addTextDataToNewImage();
    	
    }
    
    private void createBmpHeader(TextFile textInput)
    {
        textBytes = textInput.getTextData();

        int modifiedLength = textBytes.length + Constants.BMP_HEADER_SIZE + Constants.DIB_HEADER_SIZE;
        metaData = new LinkedList<ExtendedByte>();
        
        //Generate BMP Header metadata
        metaData.add(new ExtendedByte("424D", 2));  //magic number for BMP files
        metaData.add(new ExtendedByte(modifiedLength, 4));//string representing length of new file in hex
        metaData.add(new ExtendedByte(2));				  //unused field
        metaData.add(new ExtendedByte(2));						  //unused field
        metaData.add(new ExtendedByte(Constants.BMP_HEADER_SIZE + Constants.DIB_HEADER_SIZE, 4)); //offset of the pixel array
    }
    
    public void createDibHeader()
    {
    	//Generate DIBHeaderMetadata
        metaData.add(new ExtendedByte(Constants.DIB_HEADER_SIZE, 4)); //DIB header size

        //Calculate the size of the image
        double totalPixels = textBytes.length / Constants.BYTES_PER_PIXEL;
        int imageSize = (int) Math.sqrt(totalPixels);	    //find dimensions of image, rounding down
        metaData.add(new ExtendedByte(imageSize, 4)); 		//width of the image
        metaData.add(new ExtendedByte(imageSize, 4));	 //height of the image
        
        metaData.add(new ExtendedByte(1,  2));		   						//color Planes Used
        metaData.add(new ExtendedByte((Constants.BYTES_PER_PIXEL * 8 ), 2)); //bits per pixel
        metaData.add(new ExtendedByte(4));					  				//No Pixel Array Compression
        metaData.add(new ExtendedByte(textBytes.length, 4)); 				//size of Raw bitmap data.
        
        //metaData.add(new ExtendedByte(2835, 4));
        //metaData.add(new ExtendedByte(2835, 4));
        metaData.add(new ExtendedByte("0B13", 4)); 			//Print Resolution, Big Endian?
        metaData.add(new ExtendedByte("0B13", 4));
        metaData.add(new ExtendedByte(4)); 				  // number of colors in pallet
        metaData.add(new ExtendedByte(4));				  // 0 means all colors are important
    }
    
    public void addTextDataToNewImage()
    {
    	byte[] tempMetaData = ExtendedByte.convertBMPMetadata(metaData);
        
        System.out.println("TextArray " + Arrays.toString(textBytes));

        modifiedData = new byte[tempMetaData.length + textBytes.length];
        
        int index = 0;
        while(index < tempMetaData.length)
        {
        	modifiedData[index] = tempMetaData[index];
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
     * Being used to set pixel resolution.
     * 
     * 
     */
    @Deprecated
    public LinkedList<String> padHex(String hexString, int requiredHexLength)
    {
    	LinkedList<String> temp = new LinkedList<String>();
    	if(hexString.length() == 1)
    	{
    		hexString = 'Z' + hexString;
    	}
    	while ( ! hexString.isEmpty())
    	{
    		    temp.add("" +hexString.charAt(0) + hexString.charAt(1));
    		    hexString = hexString.substring(2);
    	}
    	while(temp.size() < requiredHexLength)
    	{
    		temp.add("NULL");
    	}
    	return temp;
    }

    
    /**
     * @param numberOfBytes as hex
     * @return '00' for each byte requested
     */
    @Deprecated
    public LinkedList<String> emptyBytes(int bytesNeeded)
    {
    	LinkedList<String> temp = new LinkedList<String>();
    	while(bytesNeeded > 0)
    	{
    		temp.add("NULL");
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
    @Deprecated
    public LinkedList<String> transformAndPadHex(int input, int requiredHexLength)
    {
    	LinkedList<String> temp = new LinkedList<String>();
    	
    	String hexString = Integer.toHexString(input);
    	if(hexString.length() == 1)
        	hexString = 'Z' + hexString;
    	
        temp.add(hexString);
        
        while( temp.size() < requiredHexLength )
        {
        	temp.add("NULL");
        }
        
        return temp;
    }
    
    @Deprecated
    public byte[] parseLinkedList(LinkedList<String> list)
    {
    	byte[] temp = new byte[list.size()];
    	int index = 0;
    	while(list.peek() != null)
    	{
    		String s = list.poll();
    		if(s.contains("NULL"))
    			temp[index] = (byte) Constants.byteLiteral[0];
    		else
    			if(s.contains("Z"))
    				temp[index] = Constants.byteLiteral[Integer.parseInt(s.charAt(1) +"")];
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
		
		
		TextFile text = new TextFile("testData/Vorgons.txt");
		par.CreateBmpFileFromData(text);
		par.write(par.modifiedData, "largeTest1.bmp");
		
		
	 /**
		Bitmap bmp = new Bitmap("testData/test1.bmp", par.getDataRead());
		bmp.printHeaderData();
		try{
		ImageGUI g = new ImageGUI(bmp);
		}
		catch (Exception e)
		{
			System.exit(1);
		}
	 **/
	}
	
}
