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
	
	public SearchResult(){
		result=new ArrayList<ClientGeoObject>();
		totalHits = -1;
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
		return totalHits;
	}
	
	public void setResultCount(int totalHits)
	{
		this.totalHits = totalHits;
	}
}
