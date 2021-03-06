package net.dervism.trafokanten.ws;

/**
 * 
 * @author Dervis M
 *
 */

public class Platform {
	
	private Trafikanten info;
	
	public Platform() {
		info = new Trafikanten();
	}
	
	public void start() {
		Thread trafikanten = new Thread(info);
		Passenger passenger = new Passenger();
		info.addObserver(passenger);
		trafikanten.start();
	}
	
}
