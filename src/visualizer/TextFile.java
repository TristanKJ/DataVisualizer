package visualizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class TextFile implements SupportedType {



    private byte[] textData;

    public TextFile(File file)
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            reader.close();
            textData = Parser.StringToBytesASCII(sb.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public TextFile(String filePath)
    {
        BufferedReader reader = null;
        try {
        	File file = new File(filePath);
            reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            reader.close();
            textData = Parser.StringToBytesASCII(sb.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public String getMetaData()
    {
        return "Unimplemented Method, getMetaData";
    }

	public String getTextAsString()
	{
		StringBuilder sb = new StringBuilder();
		return "";
	}
	
    public byte[] getTextData() {
        return textData;
    }

    public void setTextData(byte[] textData) {
        this.textData = textData;
    }

	@Override
	public void printHeaderData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printFileData() {
		// TODO Auto-generated method stub
		
	}
}
