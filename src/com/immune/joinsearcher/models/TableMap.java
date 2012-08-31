package com.immune.joinsearcher.models;

import java.util.HashMap;

/**
 * @author sabbirmanandhar
 * 
 * stores Maps of keys(String)/values(FieldNamesmap) pair for unique lookup table name as key
 * 
 */


public class TableMap extends HashMap<String, FieldNamesMap> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FieldNamesMap getFieldNamesMapForTable(String lookupTableName){
		return this.get(lookupTableName);
	}
	
	public void addFieldNamesMap(String lookupTableName, FieldNamesMap fieldNamesMap){
		this.put(lookupTableName, fieldNamesMap);
	}

}
