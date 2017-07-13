/**
 * 
 */
package it.polimi.ingsw.cg25.gamegenerics;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A simple logger
 * The standard usage is to have one file where all logs are stored and another with only critical information
 * @author Davide
 *
 */
public class GameLogger {
	
	/**
	 * Logger identifier
	 */
	private final String seed;
	
	/**
	 * File name for complete log
	 */
	private final String logFileName;
	
	/**
	 * File name for the exception log
	 */
	private final String exceptionFileName;
	
	/**
	 * Path for log files
	 */
	private final String logPath;
	
	/**
	 * Logger object
	 */
	private Logger logger;

	/**
	 * Minimum level to consider a message to be logged as critical
	 */
	public static final Level MIN_EX_LEVEL = Level.WARNING;
	
	/**
	 * Minimum level to consider a message to be logged (at all)
	 */
	public static final Level MIN_MV_LEVEL = Level.CONFIG;
	
	/**
	 * Default path where log files are stored
	 */
	public static final String DEFAULT_LOG_PATH = System.getProperty("user.dir")+"/src/main/resources/logs/";
	
	/**
	 * Creates a logger with no output attached
	 */
	public GameLogger(){
		this.logPath = "";
		String rand = String.valueOf(System.nanoTime());
		this.seed = "FakeLog_" + rand;
		this.exceptionFileName = this.seed + "_Ex";
		this.logFileName = this.seed + "_All";

		this.logger = Logger.getLogger(this.seed);

		this.logger.setUseParentHandlers(false);
		this.logger.setLevel(Level.ALL);
	}
	
	/**
	 * Creates a logger with two files attached (one for all log infos, one for critical infos)
	 * 
	 * @param logID
	 *            String the match id (used as seed)
	 * @param logPath
	 *            String the directory path where log files will be stored
	 */
	public GameLogger(String logID, String logPath) {
		this.logPath = logPath;
		String rand = String.valueOf(System.nanoTime());
		this.seed = logID + "_" + rand;
		this.exceptionFileName = this.seed + "_Ex";
		this.logFileName = this.seed + "_All";

		this.logger = Logger.getLogger(this.seed);

		this.logger.setUseParentHandlers(false);
		this.logger.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();

		// Attach exception file to logger
		try {
			FileHandler fhEx = new FileHandler(this.logPath + this.exceptionFileName + ".txt");
			fhEx.setFormatter(formatter);
			fhEx.setLevel(MIN_EX_LEVEL);
			this.logger.addHandler(fhEx);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		// Attach moves file to logger
		try {
			FileHandler fhMv = new FileHandler(this.logPath + this.logFileName + ".txt");
			fhMv.setFormatter(formatter);
			fhMv.setLevel(MIN_MV_LEVEL);
			this.logger.addHandler(fhMv);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attach Handler to the logger
	 * 
	 * @param handler
	 *            {@link Handler} the handler to attach
	 * @param level
	 *            {@link Level} level of the handler (see MIN_EX_LEVEL and
	 *            MAX_EX_LEVEL static attributes)
	 */
	public void attachHandler(Handler handler, Level level) {
		handler.setLevel(level);
		this.logger.addHandler(handler);
	}

	/**
	 * Log an exception
	 * 
	 * @param e
	 *            {@link Exception} The exception to log
	 * @param message
	 *            String The message to log
	 * @param level
	 *            {@link Level} The level of the log entry (Level.WARNING or
	 *            higher)
	 */
	public void log(Exception e, String message, Level level) {

		message.concat("\r\n");

		if (level.intValue() < MIN_EX_LEVEL.intValue())
			this.logger.log(MIN_EX_LEVEL, message, e);
		this.logger.log(level, message, e);

	}

	/**
	 * Log an exception
	 * 
	 * @param e
	 *            {@link Exception} the exception to log
	 */
	public void log(Exception e) {
		this.log(e, "", MIN_EX_LEVEL);
	}

	/**
	 * Log an exception
	 * 
	 * @param e
	 *            {@link Exception} the exception to log
	 * @param message
	 *            String the message to log
	 */
	public void log(Exception e, String message) {
		this.log(e, message, MIN_EX_LEVEL);
	}

	/**
	 * Log an exception
	 * 
	 * @param message
	 *            String the message to log
	 */
	public void warning(String message) {
		this.log(null, message, MIN_EX_LEVEL);
	}

	/**
	 * Log a message
	 * 
	 * @param message
	 *            String the message to log
	 * @param level
	 *            {@link Level} The level of the log entry (Level.INFO or
	 *            higher)
	 */
	public void log(String message, Level level) {

		message.concat("\r\n");

		if (level.intValue() < MIN_MV_LEVEL.intValue())
			this.logger.log(MIN_MV_LEVEL, message);

		this.logger.log(level, message);

	}

	/**
	 * Log a message
	 * 
	 * @param message
	 *            String the message to log
	 */
	public void log(String message) {
		this.log(message, MIN_MV_LEVEL);
	}

	/**
	 * Closes <b>permanently</b> all handler attached to the logger.
	 * <p>This <b>MUST</b> be called when the logger is no more needed.
	 */
	public void close() {
		for (Handler h : this.logger.getHandlers()) {
			h.close();
		}
	}
	

}
