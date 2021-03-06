package com.gmodelo.cyclicinventories.beans;

import java.util.List;

public class ConciliacionBean {
	
	private String route;
	private String type;
	private String bukrs;
	private String bukrsD;
	private String werks;
	private String werksD;
	private String justification;
	private Integer counted;
	private boolean countA;
	private boolean countB;
	private boolean count2;
	private boolean count3;
	private boolean countE;
	private int docInvId;
	private boolean sapRecount;
	
	private List<ConciliationPositionBean> positions;

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getBukrsD() {
		return bukrsD;
	}

	public void setBukrsD(String bukrsD) {
		this.bukrsD = bukrsD;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getWerksD() {
		return werksD;
	}

	public void setWerksD(String werksD) {
		this.werksD = werksD;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public int getDocInvId() {
		return docInvId;
	}

	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}

	public List<ConciliationPositionBean> getPositions() {
		return positions;
	}

	public void setPositions(List<ConciliationPositionBean> positions) {
		this.positions = positions;
	}

	public boolean isCountA() {
		return countA;
	}

	public void setCountA(boolean countA) {
		this.countA = countA;
	}

	public boolean isCountB() {
		return countB;
	}

	public void setCountB(boolean countB) {
		this.countB = countB;
	}

	public boolean isCount2() {
		return count2;
	}

	public void setCount2(boolean count2) {
		this.count2 = count2;
	}

	public boolean isCount3() {
		return count3;
	}

	public void setCount3(boolean count3) {
		this.count3 = count3;
	}

	public boolean isCountE() {
		return countE;
	}

	public void setCountE(boolean countE) {
		this.countE = countE;
	}

	public Integer getCounted() {
		return counted;
	}

	public void setCounted(Integer counted) {
		this.counted = counted;
	}

	public boolean isSapRecount() {
		return sapRecount;
	}

	public void setSapRecount(boolean sapRecount) {
		this.sapRecount = sapRecount;
	}

	@Override
	public String toString() {
		return "ConciliacionBean [route=" + route + ", type=" + type + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD
				+ ", werks=" + werks + ", werksD=" + werksD + ", justification=" + justification + ", counted="
				+ counted + ", countA=" + countA + ", countB=" + countB + ", count2=" + count2 + ", count3=" + count3
				+ ", countE=" + countE + ", docInvId=" + docInvId + ", sapRecount=" + sapRecount + ", positions="
				+ positions + "]";
	}



	public ConciliacionBean(String route, String type, String bukrs, String bukrsD, String werks, String werksD,
			String justification, Integer counted, boolean countA, boolean countB, boolean count2, boolean count3,
			boolean countE, int docInvId, boolean sapRecount, List<ConciliationPositionBean> positions) {
		super();
		this.route = route;
		this.type = type;
		this.bukrs = bukrs;
		this.bukrsD = bukrsD;
		this.werks = werks;
		this.werksD = werksD;
		this.justification = justification;
		this.counted = counted;
		this.countA = countA;
		this.countB = countB;
		this.count2 = count2;
		this.count3 = count3;
		this.countE = countE;
		this.docInvId = docInvId;
		this.sapRecount = sapRecount;
		this.positions = positions;
	}

	public ConciliacionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
