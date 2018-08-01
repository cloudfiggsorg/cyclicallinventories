package com.gmodelo.beans;

import com.bmore.ume001.beans.User;

public class LdapUser <L>{
	User user;
	L ldapBean;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public L getLdapBean() {
		return ldapBean;
	}
	public void setLdapBean(L ldapBean) {
		this.ldapBean = ldapBean;
	}
	public LdapUser(User user, L ldapBean) {
		super();
		this.user = user;
		this.ldapBean = ldapBean;
	}
	public LdapUser() {
		super();
		this.user = new User();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "LdapUser [user=" + user + ", ldapBean=" + ldapBean + "]";
	}
	
	

}
