package com.gmodelo.beans;

public class LoginBean <K> {

	String loginId;
	String loginPass;
	String loginLang;
	Integer activeInterval;
	String relationUUID;
	K lsObjectLB;
		
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPass() {
		return loginPass;
	}

	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}

	public String getRelationUUID() {
		return relationUUID;
	}

	public void setRelationUUID(String relationUUID) {
		this.relationUUID = relationUUID;
	}

	public String getLoginLang() {
		return loginLang;
	}

	public void setLoginLang(String loginLang) {
		this.loginLang = loginLang;
	}

	public Integer getActiveInterval() {
		return activeInterval;
	}

	public void setActiveInterval(Integer activeInterval) {
		this.activeInterval = activeInterval;
	}

	public K getLsObjectLB() {
		return lsObjectLB;
	}

	public void setLsObjectLB(K lsObjectLB) {
		this.lsObjectLB = lsObjectLB;
	}

	
	@Override
	public String toString() {
		return "LoginBean [loginId=" + loginId + ", loginPass=" + loginPass + ", loginLang=" + loginLang
				+ ", activeInterval=" + activeInterval + ", relationUUID=" + relationUUID + ", lsObjectLB=" + lsObjectLB
				+ "]";
	}

	public LoginBean(String loginId, String loginPass, String loginLang, Integer activeInterval, String relationUUID,
			K lsObjectLB) {
		super();
		this.loginId = loginId;
		this.loginPass = loginPass;
		this.loginLang = loginLang;
		this.activeInterval = activeInterval;
		this.relationUUID = relationUUID;
		this.lsObjectLB = lsObjectLB;
	}

	public LoginBean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
