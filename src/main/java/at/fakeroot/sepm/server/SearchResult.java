package at.fakeroot.sepm.server;

import java.util.ArrayList;

import at.fakeroot.sepm.client.serialize.ClientGeoObject;
/**
 * @author Anca Cismasiu
 * Class that stores the search result in an ArrayList of ClientGeoObjects
 * */

public class SearchResult {

	private ArrayList<ClientGeoObject> result=null;

	public SearchResult(){
		result=new ArrayList<ClientGeoObject>();
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
		return result.size();
	}
}
