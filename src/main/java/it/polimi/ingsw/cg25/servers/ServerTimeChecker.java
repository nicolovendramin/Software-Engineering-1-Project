package it.polimi.ingsw.cg25.servers;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author nicolo
 *
 */
public class ServerTimeChecker extends TimeChecker {
	
	/**
	 * The server fpr which the checker is working
	 */
	private ServerCD4 server;
	
	/**
	 * Instantiate a new ServerTimeChecker for a given server
	 * @param expirationDates the list of dates to be checked
	 * @param server is the server for which the TimeChecker performs the checks
	 */
	public ServerTimeChecker(List<Date> expirationDates,ServerCD4 server){
		super(expirationDates);
		this.server = server;
	}


	@Override
	public void run() {
		while(!this.isEnded()){
			if(this.getExpirationDates().size()!=0)
				for(int i=0;i<this.getExpirationDates().size();i++){
				if(this.getExpirationDates().get(i).before(new Date()))
				{
					try{
					server.launchGame(i);			
					}catch(IllegalArgumentException e){
						server.removePendingGame(i);
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
