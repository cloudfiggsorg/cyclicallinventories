package com.gmodelo.runtime;

import java.sql.Connection;

import com.gmodelo.beans.DocInvBean;
import com.sap.conn.jco.JCoDestination;

public class _Runtime_Ziacmf_Mov1 extends Thread {

	JCoDestination asyncDestination;
	Connection asyncConnection;
	DocInvBean docInvBean;

	public _Runtime_Ziacmf_Mov1(JCoDestination asyncDestination, Connection asyncConnection, DocInvBean docInvBean) {
		super();
		this.asyncDestination = asyncDestination;
		this.asyncConnection = asyncConnection;
		this.docInvBean = docInvBean;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

}
