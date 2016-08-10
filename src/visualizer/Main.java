/**
 * Main class Files created appear in the main folder,
 * there are sample text files in the testData folder. 
 * @author Tristan
 */


package visualizer;

public class Main {

	public static void main(String[] args) throws Exception {

		if(!args[0] == null) {
		
		String pathOfTextFile = "testData/" + args[0] + ".txt";
		String nameOfOutputFile = "Output_" + args[0] +".bmp";
		//String nameOfOutputFile = "Output_" + System.currentTimeMillis() +".bmp";
		
		Parser par = new Parser();

		TextFile text = new TextFile(pathOfTextFile);
		par.createBmpFileFromData(text);
		par.write(par.modifiedData, nameOfOutputFile);
	}
	else
		System.out.println("please enter the name of the file as a parameter.")
	
	
		
		
		//This version of Main uses arguments from command line
		/**
		Parser par = new Parser();

		TextFile text = new TextFile(args[0]);
		par.CreateBmpFileFromData(text);
		par.write(par.modifiedData, args[1]);
		**/
	}

}
