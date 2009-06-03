import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public abstract class ICrawler  {
	private BoundingBox crawlArea;
	protected BoundingBox curBox;
	
	private double XOFFSET=0.5;
	private double YOFFSET=0.5;
	
	private boolean newCircle=false;
	
	// Create an instance of HttpClient.
	private HttpClient client = new HttpClient();
	
	
	Registry registry;
	GeoSave geoSaver;
	
	public void ICrawler() {
		ICrawler(new BoundingBox(-1.0,-1.0,1.0,1.0), 0.5);
	}	
	
	public  void ICrawler(BoundingBox _crawlArea){
		ICrawler(_crawlArea, 0.5);
	}
	
	public void ICrawler(BoundingBox _crawlArea, double _jumpXYOffset) {
		crawlArea=_crawlArea;
		XOFFSET=_jumpXYOffset;
		YOFFSET=_jumpXYOffset;
		curBox = new BoundingBox(
				_crawlArea.getX1(),
				_crawlArea.getY1(),
				_crawlArea.getX1()+XOFFSET,
				_crawlArea.getY1()+YOFFSET);
		
		try {
			registry = LocateRegistry.getRegistry("10.0.0.150");
			geoSaver = (GeoSave) registry.lookup("GeoSave");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} 
		
	}
	
	public String crawl(String crawlURL){
		// Create a method instance.
		GetMethod method = new GetMethod(crawlURL);
		
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			
			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			
			
			return new String(responseBody);
			
			/*
			InputStream myStream = method.getResponseBodyAsStream();			
			BufferedReader in = new BufferedReader(new InputStreamReader(myStream));
			//in.readLine();
			String back="";
			while(in.ready()){
				back+=in.readLine();
			}
			System.out.println(back);
			*/
			
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}	
		return null;
	}
	
	public BoundingBox crawlBox(){		
		return nextBox(curBox);
	}
	
	private BoundingBox nextBox(BoundingBox _curPos){
		
		curBox=nextRight(_curPos);
		
		if(newCircle){
			//System.out.println("rein");
			curBox=nextDown(curBox);
			newCircle=false;
		}
		
		return curBox;
		
	}
	
	private BoundingBox nextRight(BoundingBox _curPos){
		if(_curPos.getX1()+XOFFSET>crawlArea.getX2()){
			//System.err.println("rumpRight "+_curPos);
			newCircle=true;
			return new BoundingBox(
					crawlArea.getX1(),
					_curPos.getY1(),
					crawlArea.getX1()+XOFFSET,
					_curPos.getY2());
		}else{
			return new BoundingBox(
				_curPos.getX2(),
				_curPos.getY1(),
				_curPos.getX2()+XOFFSET,
				_curPos.getY2());
		}
		
		
	}
	
	private BoundingBox nextDown(BoundingBox _curPos){
		if(_curPos.getY1()+YOFFSET>crawlArea.getY2()){
			//System.err.println("JumpDown ");
			return new BoundingBox(
					_curPos.getX1(),
					crawlArea.getY1(),
					_curPos.getX2(),
					crawlArea.getY1()+YOFFSET);
		}else{		
			return new BoundingBox(
				_curPos.getX1(),
				_curPos.getY2(),
				_curPos.getX2(),
				_curPos.getY2()+YOFFSET);
		}
	}
	
	private BoundingBox nextLeft(BoundingBox _curPos){
		return new BoundingBox(
				_curPos.getX1()-XOFFSET,
				_curPos.getY1(),
				_curPos.getX1(),
				_curPos.getY2());
	}
	
	private BoundingBox nextUp(BoundingBox _curPos){
		return new BoundingBox(
				_curPos.getX1(),
				_curPos.getY1()-YOFFSET,
				_curPos.getX2(),
				_curPos.getY1());
	}
	
	public String toString() {
		return curBox.toString();
	}
	
	public void saveObject(DBGeoObject[] newObjects) {		
		
		try {
			geoSaver.saveObject(newObjects);
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
	}
}
