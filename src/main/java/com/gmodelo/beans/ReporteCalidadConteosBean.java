package com.gmodelo.beans;

public class ReporteCalidadConteosBean {
	
	String zoneId;
	String zoneD;
	String lgpla;
	String matnr;
	String matnrD;
	String measureUnit;
	String count1A;
	String count1B;
	String count2;
	String count3;
	String countX;
	String calidad;
	
	public String getCalidad() {
		return calidad;
	}
	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneD() {
		return zoneD;
	}
	public void setZoneD(String zoneD) {
		this.zoneD = zoneD;
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
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public String getCount1A() {
		return count1A;
	}
	public void setCount1A(String count1a) {
		count1A = count1a;
	}
	public String getCount1B() {
		return count1B;
	}
	public void setCount1B(String count1b) {
		count1B = count1b;
	}
	public String getCount2() {
		return count2;
	}
	public void setCount2(String count2) {
		this.count2 = count2;
	}
	public String getCount3() {
		return count3;
	}
	public void setCount3(String count3) {
		this.count3 = count3;
	}
	public String getCountX() {
		return countX;
	}
	public void setCountX(String countX) {
		this.countX = countX;
	}

	
	public ReporteCalidadConteosBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReporteCalidadConteosBean(String zoneId, String zoneD, String lgpla, String matnr, String matnrD,
			String measureUnit, String count1a, String count1b, String count2, String count3, String countX,
			String calidad) {
		super();
		this.zoneId = zoneId;
		this.zoneD = zoneD;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.matnrD = matnrD;
		this.measureUnit = measureUnit;
		count1A = count1a;
		count1B = count1b;
		this.count2 = count2;
		this.count3 = count3;
		this.countX = countX;
		this.calidad = calidad;
	}
	@Override
	public String toString() {
		return "ReporteCalidadBean [zoneId=" + zoneId + ", zoneD=" + zoneD + ", lgpla=" + lgpla + ", matnr=" + matnr
				+ ", matnrD=" + matnrD + ", measureUnit=" + measureUnit + ", count1A=" + count1A + ", count1B="
				+ count1B + ", count2=" + count2 + ", count3=" + count3 + ", countX=" + countX + ", calidad=" + calidad
				+ "]";
	}
	
}
