/**
 * reads in the text file and generates the text data byte array.
 * @author Tristan
 */

package visualizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFile {

	private byte[] textData;

	public TextFile(String filePath) {
		BufferedReader reader = null;
		try {
			File file = new File(filePath);
			reader = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			textData = Parser.stringToBytesASCII(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getTextData() {
		return textData;
	}

	public void setTextData(byte[] textData) {
		this.textData = textData;
	}

}
