package com.immune.joinsearcher.models;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sabbirmanandhar
 * 
 * stores Maps of keys(String)/values(String) pair for unique combinations of field values as key
 * 
 */


public class FieldValuesMap extends HashMap<String, Map<String, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Map<String, String> getKeyValuesMapForFieldValues(String fieldValuesAsKey){
		return this.get(fieldValuesAsKey);
	}
	
	public void addKeyValuesMap(String fieldValuesAsKey, Map<String, String> fieldValuesMap){
		this.put(fieldValuesAsKey, fieldValuesMap);
	}

}
