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
	@Column(name = "updated_by")
	int updatedBy;
	@Column(name = "value")
	double value;
	@Column(name = "category_id")
	double categoryId;

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

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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

	public void setCategoryId(double categoryId) {
		this.categoryId = categoryId;
	}
}
