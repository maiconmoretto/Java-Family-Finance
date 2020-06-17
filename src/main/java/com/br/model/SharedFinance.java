package com.br.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shared_finance")
public class SharedFinance implements Serializable {

	@Id
	@Column(name = "id")
	int id;
	@Column(name = "user_id")
	int userId;
	@Column(name = "shared_user_id")
	int sharedUserId;
	@Column(name = "created_at")
	String createdAt;
	@Column(name = "accepted")
	boolean accepted;

	public SharedFinance() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSharedUserId() {
		return sharedUserId;
	}

	public void setSharedUserId(int sharedUserId) {
		this.sharedUserId = sharedUserId;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public SharedFinance(int userId, int sharedUserId, String createdAt, boolean accepted) {
		this.userId = userId;
		this.createdAt = createdAt;
		this.sharedUserId = sharedUserId;
		this.accepted = accepted;
	}
}
