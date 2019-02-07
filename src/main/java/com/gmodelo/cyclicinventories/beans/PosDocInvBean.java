package com.gmodelo.cyclicinventories.beans;

import java.util.ArrayList;
import java.util.Date;

public class PosDocInvBean {
		
	private int posId;
	private int doncInvId;
	private int item;
	private String lgort;
	private String lgortD;
	private String lgtyp;
	private String ltypt;
	private String lgNum;
	private String lgpla;
	private String matnr;
	private String matnrD;
	private String category;
	private String costByUnit;
	private String meins;
	private String theoric;
	private String counted;
	private String countedExpl;
	private String diff;
	private String imwmMarker;
	private String consignation;
	private String transit;
	private Date dCounted;
	private boolean isExplosion;
	private ArrayList<Justification> lsJustification;
	
	public PosDocInvBean(){
		super();
		this.lsJustification = new ArrayList<>();
	}

	public PosDocInvBean(int posId, int doncInvId, int item, String lgort, String lgortD, String lgtyp, String ltypt,
			String lgNum, String lgpla, String matnr, String matnrD, String category, String costByUnit, String meins,
			String theoric, String counted, String countedExpl, String diff, String imwmMarker, String consignation,
			String transit, Date dCounted, boolean isExplosion, ArrayList<Justification> lsJustification) {
		super();
		this.posId = posId;
		this.doncInvId = doncInvId;
		this.item = item;
		this.lgort = lgort;
		this.lgortD = lgortD;
		this.lgtyp = lgtyp;
		this.ltypt = ltypt;
		this.lgNum = lgNum;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.matnrD = matnrD;
		this.category = category;
		this.costByUnit = costByUnit;
		this.meins = meins;
		this.theoric = theoric;
		this.counted = counted;
		this.countedExpl = countedExpl;
		this.diff = diff;
		this.imwmMarker = imwmMarker;
		this.consignation = consignation;
		this.transit = transit;
		this.dCounted = dCounted;
		this.isExplosion = isExplosion;
		this.lsJustification = lsJustification;
	}

	public int getPosId() {
		return posId;
	}

	public void setPosId(int posId) {
		this.posId = posId;
	}

	public int getDoncInvId() {
		return doncInvId;
	}

	public void setDoncInvId(int doncInvId) {
		this.doncInvId = doncInvId;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getLgortD() {
		return lgortD;
	}

	public void setLgortD(String lgortD) {
		this.lgortD = lgortD;
	}

	public String getLgtyp() {
		return lgtyp;
	}

	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}

	public String getLtypt() {
		return ltypt;
	}

	public void setLtypt(String ltypt) {
		this.ltypt = ltypt;
	}

	public String getLgNum() {
		return lgNum;
	}

	public void setLgNum(String lgNum) {
		this.lgNum = lgNum;
	}

	public String getLgpla() {
		return lgpla;
	}

	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getMatnrD() {
		return matnrD;
	}

	public void setMatnrD(String matnrD) {
		this.matnrD = matnrD;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCostByUnit() {
		return costByUnit;
	}

	public void setCostByUnit(String costByUnit) {
		this.costByUnit = costByUnit;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getTheoric() {
		return theoric;
	}

	public void setTheoric(String theoric) {
		this.theoric = theoric;
	}

	public String getCounted() {
		return counted;
	}

	public void setCounted(String counted) {
		this.counted = counted;
	}

	public String getCountedExpl() {
		return countedExpl;
	}

	public void setCountedExpl(String countedExpl) {
		this.countedExpl = countedExpl;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public String getImwmMarker() {
		return imwmMarker;
	}

	public void setImwmMarker(String imwmMarker) {
		this.imwmMarker = imwmMarker;
	}

	public String getConsignation() {
		return consignation;
	}

	public void setConsignation(String consignation) {
		this.consignation = consignation;
	}

	public String getTransit() {
		return transit;
	}

	public void setTransit(String transit) {
		this.transit = transit;
	}

	public Date getdCounted() {
		return dCounted;
	}

	public void setdCounted(Date dCounted) {
		this.dCounted = dCounted;
	}

	public boolean isExplosion() {
		return isExplosion;
	}

	public void setExplosion(boolean isExplosion) {
		this.isExplosion = isExplosion;
	}

	public ArrayList<Justification> getLsJustification() {
		return lsJustification;
	}

	public void setLsJustification(ArrayList<Justification> lsJustification) {
		this.lsJustification = lsJustification;
	}

	@Override
	public String toString() {
		return "PosDocInvBean [posId=" + posId + ", doncInvId=" + doncInvId + ", item=" + item + ", lgort=" + lgort
				+ ", lgortD=" + lgortD + ", lgtyp=" + lgtyp + ", ltypt=" + ltypt + ", lgNum=" + lgNum + ", lgpla="
				+ lgpla + ", matnr=" + matnr + ", matnrD=" + matnrD + ", category=" + category + ", costByUnit="
				+ costByUnit + ", meins=" + meins + ", theoric=" + theoric + ", counted=" + counted + ", countedExpl="
				+ countedExpl + ", diff=" + diff + ", imwmMarker=" + imwmMarker + ", consignation=" + consignation
				+ ", transit=" + transit + ", dCounted=" + dCounted + ", isExplosion=" + isExplosion
				+ ", lsJustification=" + lsJustification + "]";
	}

}
