/**
 * Class that handles the creation of new metadata
 */

package visualizer;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Parser {
	
	byte[] textBytes;
	byte[] modifiedData;
	LinkedList<ExtendedByte> metaData;
	ExtendedByte eb = new ExtendedByte();

	public Parser() {
		// System.out.println(System.getProperty("user.dir"));
	}

	
	/**
	 * Transforms a string into a byte array that has been encoded as ASCII
	 * @param str Input to be transformed
	 * @return byte array of str
	 */
	public static byte[] stringToBytesASCII(String str) {
		byte[] b = str.getBytes(StandardCharsets.US_ASCII);
		return b;
	}
	
	/**
	 * Master method for creating and adding each part
	 * of the bmp file to the list that holds the new file.
	 * @param textToBeConverted
	 */
	public void createBmpFileFromData(TextFile textToBeConverted) {
		createBmpHeader(textToBeConverted);
		createDibHeader();
		addTextDataToNewImage();
	}

	private void createBmpHeader(TextFile textInput) {
		textBytes = textInput.getTextData();

		int modifiedLength = textBytes.length + Constants.BMP_HEADER_SIZE
				+ Constants.DIB_HEADER_SIZE;
		metaData = new LinkedList<ExtendedByte>();

		// Generate BMP Header metadata
		metaData.add(new ExtendedByte("424D", 2)); // magic number for BMP files
		metaData.add(new ExtendedByte(modifiedLength, 4));// string representing
															// length of new
															// file in hex
		metaData.add(new ExtendedByte(2)); // unused field
		metaData.add(new ExtendedByte(2)); // unused field
		metaData.add(new ExtendedByte(Constants.BMP_HEADER_SIZE
				+ Constants.DIB_HEADER_SIZE, 4)); // offset of the pixel array
	}

	private void createDibHeader() {
		// Generate DIBHeaderMetadata
		metaData.add(new ExtendedByte(Constants.DIB_HEADER_SIZE, 4)); // DIB
																		// header
																		// size

		// Calculate the size of the image
		double totalPixels = textBytes.length / Constants.BITS_PER_PIXEL;
		int imageSize = (int) Math.sqrt(totalPixels); // find dimensions of
														// image, rounding down
		metaData.add(new ExtendedByte(imageSize, 4)); // width of the image
		metaData.add(new ExtendedByte(imageSize, 4)); // height of the image

		metaData.add(new ExtendedByte(1, 2)); // color Planes Used
		metaData.add(new ExtendedByte((Constants.BITS_PER_PIXEL), 2)); // bits
																			// per
																			// pixel
		metaData.add(new ExtendedByte(4)); // No Pixel Array Compression
		metaData.add(new ExtendedByte(textBytes.length, 4)); // size of Raw
																// bitmap data.

		metaData.add(new ExtendedByte(Constants.PRINT_RESOLUTION, 4)); // Print
		metaData.add(new ExtendedByte(Constants.PRINT_RESOLUTION, 4));// Resolution
		
		metaData.add(new ExtendedByte(4)); // number of colors in pallet
		metaData.add(new ExtendedByte(4)); // 0 means all colors are important
	}

	private void addTextDataToNewImage() {
		byte[] tempMetaData = ExtendedByte.convertBMPMetadata(metaData);

		modifiedData = new byte[tempMetaData.length + textBytes.length];

		int index = 0;
		while (index < tempMetaData.length) {
			modifiedData[index] = tempMetaData[index];
			index++;
		}
		int index2 = 0;
		while (index2 < textBytes.length) {
			modifiedData[index] = textBytes[index2];
			index++;
			index2++;
		}
	}

	public void write(byte[] input, String outputFileName) {
		// writing binary file.
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(
						outputFileName));
				output.write(input);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public byte[] getDataRead() {
		byte[] temp = textBytes.clone();
		return temp;
	}

}
