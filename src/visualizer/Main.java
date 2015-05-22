/**
 * Main class Files created appear in the main folder, there are a variety of text
 * files in the testData folder.
 */
package visualizer;


public class Main {

	public static void main(String[] args) throws Exception {

		String pathOfTextFile = "testData/Vorgons.txt";
		String nameOfOutputFile = "Output_" + System.currentTimeMillis() +".bmp";
		
		Parser par = new Parser();

		TextFile text = new TextFile(pathOfTextFile);
		par.CreateBmpFileFromData(text);
		par.write(par.modifiedData, nameOfOutputFile);
		
		
		
		//This version of Main uses arguments from command line
		/**
		Parser par = new Parser();

		TextFile text = new TextFile(args[0]);
		par.CreateBmpFileFromData(text);
		par.write(par.modifiedData, args[1]);
		**/
	}

}