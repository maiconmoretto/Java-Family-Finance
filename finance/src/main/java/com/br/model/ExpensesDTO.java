package com.br.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class ExpensesDTO {

	@Column(name = "description")
	String description;
	@Column(name = "created_by")
	int createdBy;
	@Column(name = "value")
	double value;
	@Column(name = "category_id")
	int categoryId;

	public ExpensesDTO() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public ExpensesDTO(String description, int categoryId, double value, int createdBy) {
		this.description = description;
		this.categoryId = categoryId;
		this.value = value;
		this.createdBy = createdBy;
	}

	public Expenses changeToObject() {
		return new Expenses(description, categoryId, value, createdBy);
	}
}
