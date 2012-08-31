package com.immune.joinsearcher.models;

import com.immune.joinsearcher.models.constants.JoinOperators;

public class JoinField {
	
	private String fromField;
	private String toField;
	private JoinOperators joinOperator;

	public JoinField(String fromField, String toField) {
		this.fromField = fromField;
		this.toField = toField;
		this.joinOperator = JoinOperators.EQUAL;
	}
	
	public JoinField(String fromField, String toField, JoinOperators joinOperator) {
		this.fromField = fromField;
		this.toField = toField;
		this.joinOperator = joinOperator;
	}

	public String getFromField() {
		return this.fromField;
	}

	public String getToField() {
		return this.toField;
	}
	
	public JoinOperators getJoinOperator() {
		return this.joinOperator;
	}

}
