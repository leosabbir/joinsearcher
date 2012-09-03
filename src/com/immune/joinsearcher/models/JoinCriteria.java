package com.immune.joinsearcher.models;

import java.util.List;

import com.immune.joinsearcher.models.constants.IndexTables;

public class JoinCriteria {
	
	private IndexTables lookupTable;
	private List<JoinField> joinFields;
	
	public JoinCriteria(IndexTables lookupTable, List<JoinField> joinFields){
		this.lookupTable = lookupTable;
		this.setJoinFields(joinFields);
	}
	
	public IndexTables getLookupTable() {
		return lookupTable;
	}
	public void setLookupTable(IndexTables lookupTable) {
		this.lookupTable = lookupTable;
	}

	public List<JoinField> getJoinFields() {
		return joinFields;
	}

	public void setJoinFields(List<JoinField> joinFields) {
		this.joinFields = joinFields;
	}
	
}
