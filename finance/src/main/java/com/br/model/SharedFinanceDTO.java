package com.br.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class SharedFinanceDTO {

	@Column(name = "user_id")
	int userId;
	@Column(name = "shared_user_id")
	int sharedUserId;
	@Column(name = "accepted")
	boolean accepted;

	public SharedFinanceDTO() {
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

	public SharedFinanceDTO(int userId, int sharedUserId, boolean accepted) {
		this.userId = userId;
		this.sharedUserId = sharedUserId;
		this.accepted = accepted;
	}
	
	public SharedFinance changeToObject(){
	    return new SharedFinance(userId, sharedUserId, accepted);
	}
}
