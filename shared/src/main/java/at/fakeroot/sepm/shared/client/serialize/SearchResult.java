package at.fakeroot.sepm.shared.client.serialize;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * @author Anca Cismasiu
 * Class that stores the search result in an ArrayList of ClientGeoObjects
 * */

public class SearchResult implements Serializable {
	/// default serial version id
	private static final long serialVersionUID = 1L;

	private ArrayList<ClientGeoObject> result=null;
	private int totalHits;
	private String errMsg = null;
	private int countLimit;
	
	/**
	 * StandardConstruction. Is required for the serializeation
	 */
	public SearchResult() 
	{
	}
	
	/**
	 * Constructor
	 * @param maxCount int limit for the results which are displayed (relevant for GUI ResultInfoBox)
	 */
	public SearchResult(int maxCount){
		result=new ArrayList<ClientGeoObject>();
		totalHits = -1;
		this.countLimit = maxCount;
	}

	/**
	 * Method used for adding a result element to the result list
	 * @param object ClientGeoObject element to be added 
	 * */
	public void addResultToList(ClientGeoObject object){
		result.add(object);
	}

	/**
	 * Getter for the result Arraylist
	 * @return ArrayList<ClientGeoObject> search results 
	 * */
	public ArrayList<ClientGeoObject> getResults(){
		return result;
	}

	/**
	 * Getter for the number of search results 
	 * @return int number of results
	 * */
	public int getResultCount(){
		return (totalHits);
	}
	
	/**
	 * Setter for the number of hits for a search in the DB
	 * @param totalHits int number of hits
	 */
	public void setResultCount(int totalHits)
	{
		this.totalHits = totalHits;
	}
	
	/**
	 * Getter for the limit of results which are displayed in ResultInfoBox (GUI) as total
	 * total hits in the DB
	 * @return int the limit
	 */
	public int getCountLimit()
	{
		return (countLimit);
	}
	
	public boolean hasError() {
		return errMsg != null;
	}
	
	public String getErrorMessage() {
		return errMsg;
	}
	
	public void setErrorMessage(String msg) {
		errMsg = msg;
	}
}
