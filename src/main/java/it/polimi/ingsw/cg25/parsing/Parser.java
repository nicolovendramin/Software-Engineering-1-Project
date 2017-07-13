package it.polimi.ingsw.cg25.parsing;

import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;

/**
 * 
 * @author Giovanni
 *
 */
public interface Parser {

	/**
	 * This method should parse a particular file line by line if implemented
	 * @param file the FileInputStream to parse
	 * @throws IOException 
	 * @throws CannotCreateGameException 
	 */
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException, CannotCreateGameException;
	
	/*
	 * Per testare questo metodo usando i file.txt impiegare la seguente sintassi:
	 * InputStreamReader inStrRdr = new InputStreamReader(new FileInputStream("src/main/resources/file.txt"));
	 * Passare a parseLineByLine "inStrRdr"
	 */
}
