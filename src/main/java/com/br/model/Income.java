package com.br.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "income")
public class Income implements Serializable {

	@Id
	@Column(name = "id")
	int id;
	@Column(name = "description")
	String description;
	@Column(name = "value")
	double value;
	@Column(name = "created_at")
	String createdAt;
	@Column(name = "updated_at")
	String updatedAt;
	@Column(name = "created_by")
	int createdBy;

	public Income() {
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Income(String description, String createdAt, int createdBy, double value) {
		this.description = description;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.value = value;
	}
}
