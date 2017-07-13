package it.polimi.ingsw.cg25.clients;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 * 
 * @author nicolo
 *
 */
public class Launcher {

	/**
	 * The output writer
	 */
	private PrintWriter writer;

	/**
	 * Main method to start the launcher
	 * @param args, not requested nor used
	 * @throws IOException if something goes wrong with the output stream
	 * @throws InterruptedException if there are problems launching the cli
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Launcher launcher = new Launcher();
		launcher.startLauncher();
	}
	
	/**
	 * This method initialized the launcher. You will be able to choose if you want to play
	 * the game from the command line interface or from the graphical user interface
	 * @throws InterruptedException if there are problem launching the cli
	 */
	private void startLauncher() throws IOException, InterruptedException {
		Scanner in = new Scanner(System.in);
		this.writer = new PrintWriter(System.out);
		
		boolean end = false;
		
		while(!end) {
			writer.println("Do you want to play from CLI or open a GUI?");
			writer.flush();
			String normReply = in.nextLine();
			String reply = normReply.toLowerCase();
			if("cli".equals(reply)) {
				writer.println("Launching a Command Line Interface...\n");
				writer.flush();
				end = true;
				CliSimpleClient.main(null);
			}
			else if("gui".equals(reply)){
				writer.println("Launching a Graphical User Interface...");
				writer.flush();
				end = true;
				//Lancia il thread della gui
				GuiSimpleClient.main(null);
				writer.println("Loading GUI...\n");
				writer.flush();
			}
			else if("quit".equals(reply))	
				end = true;
			else {
				writer.println("Command not found!\n");
				writer.flush();
			}
		}
		in.close();
		writer.println("Closing Launcher...");
		writer.flush();
	}
}
