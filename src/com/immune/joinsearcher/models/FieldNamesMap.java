package com.immune.joinsearcher.models;

import java.util.HashMap;

/**
 * @author sabbirmanandhar
 * 
 * stores Maps of keys(String)/values(FieldValuesMap) pair for unique combinations of field names as key
 * 
 */

public class FieldNamesMap extends HashMap<String, FieldValuesMap> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FieldValuesMap getFieldValuesMap(String fieldNamesAsKey){
		return this.get(fieldNamesAsKey);
	}
	
	public void addFieldValuesMap(String fieldNamesAsKey, FieldValuesMap fieldValuesMap){
		this.put(fieldNamesAsKey, fieldValuesMap);
	}

}
