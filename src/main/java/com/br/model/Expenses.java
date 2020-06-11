package com.br.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expenses implements Serializable {
  
	@Id
	@Column(name = "id")
	int id;
	@Column(name = "description")
	String description;
	@Column(name = "created_at")
	String createdAt;
	@Column(name = "created_by")
	int createdBy;
	@Column(name = "value")
	double value;
	@Column(name = "category_id")
	int categoryId;

	public Expenses() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
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
	
	public Expenses(String description, String createdAt, int categoryId, double value, int createdBy) {
		  this.description = description;
		  this.createdAt = createdAt;
		  this.categoryId = categoryId;
		  this.value = value;
		  this.createdBy = createdBy;
	    }
}
