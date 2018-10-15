package com.gmodelo.beans;

public class ReporteDocInvBean {
	String lgort;
	String lgortD;
	String lgtyp;
	String ltypt;
	String lgpla;
	String matnr;
	String matnrD;
	String theoric;
	String counted;
	String diff;
	String imwmMarker;

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

	public ReporteDocInvBean(String lgort, String lgortD, String lgtyp, String ltypt, String lgpla, String matnr,
			String matnrD, String theoric, String counted, String diff, String imwmMarker) {
		super();
		this.lgort = lgort;
		this.lgortD = lgortD;
		this.lgtyp = lgtyp;
		this.ltypt = ltypt;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.matnrD = matnrD;
		this.theoric = theoric;
		this.counted = counted;
		this.diff = diff;
		this.imwmMarker = imwmMarker;
	}

	public ReporteDocInvBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ReporteDocInvBean [lgort=" + lgort + ", lgortD=" + lgortD + ", lgtyp=" + lgtyp + ", ltypt=" + ltypt
				+ ", lgpla=" + lgpla + ", matnr=" + matnr + ", matnrD=" + matnrD + ", theoric=" + theoric + ", counted="
				+ counted + ", diff=" + diff + ", imwmMarker=" + imwmMarker + "]";
	}

	public String supString() {
		return lgort + "," + lgortD + "," + lgtyp + "," + ltypt + "," + lgpla + "," + matnr + "," + matnrD + ","
				+ theoric==null?"":theoric + "," + counted + "," + diff==null?"":diff + "," + imwmMarker;
	}

	public String supHeadString() {
		return "Almacen, Descripcion Almacen,Tipo Almacen,Desc. Tipo Almacen,Carril,"
				+ "Material,Desc. Material,Teorico,Contado,Diferencia(Teorico/Contado),Marcador Tipo Almacen";
	}

}
