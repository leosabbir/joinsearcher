package com.immune.joinsearcher.models;

import com.immune.joinsearcher.models.constants.IndexTables;

public class JoinCriteria {
	
	private IndexTables lookupTable;
	private String[] fromFields;
	private String[] toFields;
	
	public JoinCriteria(IndexTables lookupTable, String[] fromFields, String[] toFields){
		this.lookupTable = lookupTable;
		this.fromFields = fromFields;
		this.toFields = toFields;
	}
	
	public IndexTables getLookupTable() {
		return lookupTable;
	}
	public void setLookupTable(IndexTables lookupTable) {
		this.lookupTable = lookupTable;
	}
	public String[] getFromFields() {
		return fromFields;
	}
	public void setFromFields(String[] fromFields) {
		this.fromFields = fromFields;
	}
	public String[] getToFields() {
		return toFields;
	}
	public void setToFields(String[] toFields) {
		this.toFields = toFields;
	}
	
	

}
