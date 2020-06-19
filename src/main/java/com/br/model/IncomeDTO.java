package com.br.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class IncomeDTO {

	@Column(name = "description")
	String description;
	@Column(name = "value")
	double value;
	@Column(name = "created_by")
	int createdBy;

	public IncomeDTO() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public IncomeDTO(String description, int createdBy, double value) {
		this.description = description;
		this.createdBy = createdBy;
		this.value = value;
	}
	
	public Income changeToObject(){
	    return new Income(description, createdBy, value);
	}
}
