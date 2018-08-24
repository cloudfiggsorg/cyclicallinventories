package com.gmodelo.beans;

public class UserB {
	String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserB(String userId) {
		super();
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserB [userId=" + userId + "]";
	}
	

}
