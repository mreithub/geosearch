package at.fakeroot.sepm.crawler;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;




public class ClientRPC {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws  NotBoundException, IOException   {
		System.out.println("clientRPC");
		
	    new LocrCrawler();
	}
	

}



