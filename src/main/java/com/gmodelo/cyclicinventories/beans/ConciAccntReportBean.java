package com.gmodelo.cyclicinventories.beans;

public class ConciAccntReportBean {
	
	String docInvId;
	String bukrs;
	String werks;
	String type;
	String dateIni;
	String dateFin;
	Double accountant;
	Double accDiff;
	String percAccDiff;
	Double justification;
	String percJustification;
	
	
	public String getDocInvId() {
		return docInvId;
	}


	public void setDocInvId(String docInvId) {
		this.docInvId = docInvId;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateIni() {
		return dateIni;
	}

	public void setDateIni(String dateIni) {
		this.dateIni = dateIni;
	}

	public String getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}

	public Double getAccountant() {
		return accountant;
	}

	public void setAccountant(Double accountant) {
		this.accountant = accountant;
	}

	public Double getAccDiff() {
		return accDiff;
	}

	public void setAccDiff(Double accDiff) {
		this.accDiff = accDiff;
	}

	public String getPercAccDiff() {
		return percAccDiff;
	}

	public void setPercAccDiff(String percAccDiff) {
		this.percAccDiff = percAccDiff;
	}

	public Double getJustification() {
		return justification;
	}

	public void setJustification(Double justification) {
		this.justification = justification;
	}

	public String getPercJustification() {
		return percJustification;
	}

	public void setPercJustification(String percJustification) {
		this.percJustification = percJustification;
	}

	
	public ConciAccntReportBean() {
		super();
	}


	public ConciAccntReportBean(String docInvId, String bukrs, String werks, String type, String dateIni,
			String dateFin, Double accountant, Double accDiff, String percAccDiff, Double justification,
			String percJustification) {
		super();
		this.docInvId = docInvId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.type = type;
		this.dateIni = dateIni;
		this.dateFin = dateFin;
		this.accountant = accountant;
		this.accDiff = accDiff;
		this.percAccDiff = percAccDiff;
		this.justification = justification;
		this.percJustification = percJustification;
	}


	@Override
	public String toString() {
		return "ConciAccntReportBean [docInvId=" + docInvId + ", bukrs=" + bukrs + ", werks=" + werks + ", type=" + type
				+ ", dateIni=" + dateIni + ", dateFin=" + dateFin + ", accountant=" + accountant + ", accDiff="
				+ accDiff + ", percAccDiff=" + percAccDiff + ", justification=" + justification + ", percJustification="
				+ percJustification + "]";
	}
	
	
	
	
}
