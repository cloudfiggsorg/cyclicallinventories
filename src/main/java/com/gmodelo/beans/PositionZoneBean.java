package com.gmodelo.beans;

public class PositionZoneBean {
	
	Integer zoneId;		//Id de zona
	String positionId;	//Id de posicion
	String lgtyp;		//Tipo de Almacen
	String lgpla;		//Ubicacion
	String secuency;	//secuencia
	String imwm;
	
	public String getImwm() {
		return imwm;
	}
	public void setImwm(String imwm) {
		this.imwm = imwm;
	}
	public Integer getZoneId() {
		return zoneId;
	}
	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}
	public String getLgpla() {
		return lgpla;
	}
	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}
	public String getSecuency() {
		return secuency;
	}
	public void setSecuency(String secuency) {
		this.secuency = secuency;
	}
	
	public PositionZoneBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PositionZoneBean(Integer zoneId, String positionId, String lgtyp, String lgpla, String secuency,
			String imwm) {
		super();
		this.zoneId = zoneId;
		this.positionId = positionId;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
		this.imwm = imwm;
	}
	@Override
	public String toString() {
		return "PositionZoneBean [zoneId=" + zoneId + ", positionId=" + positionId + ", lgtyp=" + lgtyp + ", lgpla="
				+ lgpla + ", secuency=" + secuency + ", imwm=" + imwm + "]";
	}
	
	
	
	

}
