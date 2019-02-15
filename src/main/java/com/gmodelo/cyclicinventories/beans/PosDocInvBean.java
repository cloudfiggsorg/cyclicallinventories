package com.gmodelo.cyclicinventories.beans;

import java.util.ArrayList;
import java.util.Date;

public class PosDocInvBean {

	private int posId;
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
	private String countedTot;
	private String diff;
	private String imwmMarker;
	private String consignation;
	private String transit;
	private Date dCounted;
	private boolean explosion;
	private ArrayList<Justification> lsJustification;
	private String countedCost;
	private String theoricCost;
	private String diffCost;
	private Long dateIniCounted;
	private Long dateEndCounted;
	private String vhilm;
	private String vhilmCounted;
	private boolean grouped;

	public PosDocInvBean() {
		super();
		this.lsJustification = new ArrayList<>();
	}

	public int getPosId() {
		return posId;
	}

	public void setPosId(int posId) {
		this.posId = posId;
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

	public String getCountedTot() {
		return countedTot;
	}

	public void setCountedTot(String countedTot) {
		this.countedTot = countedTot;
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
		return explosion;
	}

	public void setExplosion(boolean explosion) {
		this.explosion = explosion;
	}

	public ArrayList<Justification> getLsJustification() {
		return lsJustification;
	}

	public void setLsJustification(ArrayList<Justification> lsJustification) {
		this.lsJustification = lsJustification;
	}

	public String getCountedCost() {
		return countedCost;
	}

	public void setCountedCost(String countedCost) {
		this.countedCost = countedCost;
	}

	public String getTheoricCost() {
		return theoricCost;
	}

	public void setTheoricCost(String theoricCost) {
		this.theoricCost = theoricCost;
	}

	public String getDiffCost() {
		return diffCost;
	}

	public void setDiffCost(String diffCost) {
		this.diffCost = diffCost;
	}

	public Long getDateIniCounted() {
		return dateIniCounted;
	}

	public void setDateIniCounted(Long dateIniCounted) {
		this.dateIniCounted = dateIniCounted;
	}

	public Long getDateEndCounted() {
		return dateEndCounted;
	}

	public void setDateEndCounted(Long dateEndCounted) {
		this.dateEndCounted = dateEndCounted;
	}

	public String getVhilm() {
		return vhilm;
	}

	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
	}

	public String getVhilmCounted() {
		return vhilmCounted;
	}

	public void setVhilmCounted(String vhilmCounted) {
		this.vhilmCounted = vhilmCounted;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}

	public PosDocInvBean(int posId, int doncInvId, int item, String lgort, String lgortD, String lgtyp, String ltypt,
			String lgNum, String lgpla, String matnr, String matnrD, String category, String costByUnit, String meins,
			String theoric, String counted, String countedExpl, String countedTot, String diff, String imwmMarker,
			String consignation, String transit, Date dCounted, boolean explosion,
			ArrayList<Justification> lsJustification, String countedCost, String theoricCost, String diffCost,
			Long dateIniCounted, Long dateEndCounted, String vhilm, String vhilmCounted) {
		super();
		this.posId = posId;
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
		this.countedTot = countedTot;
		this.diff = diff;
		this.imwmMarker = imwmMarker;
		this.consignation = consignation;
		this.transit = transit;
		this.dCounted = dCounted;
		this.explosion = explosion;
		this.lsJustification = lsJustification;
		this.countedCost = countedCost;
		this.theoricCost = theoricCost;
		this.diffCost = diffCost;
		this.dateIniCounted = dateIniCounted;
		this.dateEndCounted = dateEndCounted;
		this.vhilm = vhilm;
		this.vhilmCounted = vhilmCounted;
	}

	@Override
	public String toString() {
		return "PosDocInvBean [posId=" + posId + ", item=" + item + ", lgort=" + lgort + ", lgortD=" + lgortD
				+ ", lgtyp=" + lgtyp + ", ltypt=" + ltypt + ", lgNum=" + lgNum + ", lgpla=" + lgpla + ", matnr=" + matnr
				+ ", matnrD=" + matnrD + ", category=" + category + ", costByUnit=" + costByUnit + ", meins=" + meins
				+ ", theoric=" + theoric + ", counted=" + counted + ", countedExpl=" + countedExpl + ", countedTot="
				+ countedTot + ", diff=" + diff + ", imwmMarker=" + imwmMarker + ", consignation=" + consignation
				+ ", transit=" + transit + ", dCounted=" + dCounted + ", explosion=" + explosion + ", lsJustification="
				+ lsJustification + ", countedCost=" + countedCost + ", theoricCost=" + theoricCost + ", diffCost="
				+ diffCost + ", dateIniCounted=" + dateIniCounted + ", dateEndCounted=" + dateEndCounted + ", vhilm="
				+ vhilm + ", vhilmCounted=" + vhilmCounted + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((consignation == null) ? 0 : consignation.hashCode());
		result = prime * result + ((costByUnit == null) ? 0 : costByUnit.hashCode());
		result = prime * result + ((counted == null) ? 0 : counted.hashCode());
		result = prime * result + ((countedCost == null) ? 0 : countedCost.hashCode());
		result = prime * result + ((countedExpl == null) ? 0 : countedExpl.hashCode());
		result = prime * result + ((countedTot == null) ? 0 : countedTot.hashCode());
		result = prime * result + ((dCounted == null) ? 0 : dCounted.hashCode());
		result = prime * result + ((dateEndCounted == null) ? 0 : dateEndCounted.hashCode());
		result = prime * result + ((dateIniCounted == null) ? 0 : dateIniCounted.hashCode());
		result = prime * result + ((diff == null) ? 0 : diff.hashCode());
		result = prime * result + ((diffCost == null) ? 0 : diffCost.hashCode());
		result = prime * result + (explosion ? 1231 : 1237);
		result = prime * result + ((imwmMarker == null) ? 0 : imwmMarker.hashCode());
		result = prime * result + item;
		result = prime * result + ((lgNum == null) ? 0 : lgNum.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((lgortD == null) ? 0 : lgortD.hashCode());
		result = prime * result + ((lgpla == null) ? 0 : lgpla.hashCode());
		result = prime * result + ((lgtyp == null) ? 0 : lgtyp.hashCode());
		result = prime * result + ((lsJustification == null) ? 0 : lsJustification.hashCode());
		result = prime * result + ((ltypt == null) ? 0 : ltypt.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((matnrD == null) ? 0 : matnrD.hashCode());
		result = prime * result + ((meins == null) ? 0 : meins.hashCode());
		result = prime * result + posId;
		result = prime * result + ((theoric == null) ? 0 : theoric.hashCode());
		result = prime * result + ((theoricCost == null) ? 0 : theoricCost.hashCode());
		result = prime * result + ((transit == null) ? 0 : transit.hashCode());
		result = prime * result + ((vhilm == null) ? 0 : vhilm.hashCode());
		result = prime * result + ((vhilmCounted == null) ? 0 : vhilmCounted.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PosDocInvBean other = (PosDocInvBean) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (consignation == null) {
			if (other.consignation != null)
				return false;
		} else if (!consignation.equals(other.consignation))
			return false;
		if (costByUnit == null) {
			if (other.costByUnit != null)
				return false;
		} else if (!costByUnit.equals(other.costByUnit))
			return false;
		if (counted == null) {
			if (other.counted != null)
				return false;
		} else if (!counted.equals(other.counted))
			return false;
		if (countedCost == null) {
			if (other.countedCost != null)
				return false;
		} else if (!countedCost.equals(other.countedCost))
			return false;
		if (countedExpl == null) {
			if (other.countedExpl != null)
				return false;
		} else if (!countedExpl.equals(other.countedExpl))
			return false;
		if (countedTot == null) {
			if (other.countedTot != null)
				return false;
		} else if (!countedTot.equals(other.countedTot))
			return false;
		if (dCounted == null) {
			if (other.dCounted != null)
				return false;
		} else if (!dCounted.equals(other.dCounted))
			return false;
		if (dateEndCounted == null) {
			if (other.dateEndCounted != null)
				return false;
		} else if (!dateEndCounted.equals(other.dateEndCounted))
			return false;
		if (dateIniCounted == null) {
			if (other.dateIniCounted != null)
				return false;
		} else if (!dateIniCounted.equals(other.dateIniCounted))
			return false;
		if (diff == null) {
			if (other.diff != null)
				return false;
		} else if (!diff.equals(other.diff))
			return false;
		if (diffCost == null) {
			if (other.diffCost != null)
				return false;
		} else if (!diffCost.equals(other.diffCost))
			return false;
		if (explosion != other.explosion)
			return false;
		if (imwmMarker == null) {
			if (other.imwmMarker != null)
				return false;
		} else if (!imwmMarker.equals(other.imwmMarker))
			return false;
		if (item != other.item)
			return false;
		if (lgNum == null) {
			if (other.lgNum != null)
				return false;
		} else if (!lgNum.equals(other.lgNum))
			return false;
		if (lgort == null) {
			if (other.lgort != null)
				return false;
		} else if (!lgort.equals(other.lgort))
			return false;
		if (lgortD == null) {
			if (other.lgortD != null)
				return false;
		} else if (!lgortD.equals(other.lgortD))
			return false;
		if (lgpla == null) {
			if (other.lgpla != null)
				return false;
		} else if (!lgpla.equals(other.lgpla))
			return false;
		if (lgtyp == null) {
			if (other.lgtyp != null)
				return false;
		} else if (!lgtyp.equals(other.lgtyp))
			return false;
		if (lsJustification == null) {
			if (other.lsJustification != null)
				return false;
		} else if (!lsJustification.equals(other.lsJustification))
			return false;
		if (ltypt == null) {
			if (other.ltypt != null)
				return false;
		} else if (!ltypt.equals(other.ltypt))
			return false;
		if (matnr == null) {
			if (other.matnr != null)
				return false;
		} else if (!matnr.equals(other.matnr))
			return false;
		if (matnrD == null) {
			if (other.matnrD != null)
				return false;
		} else if (!matnrD.equals(other.matnrD))
			return false;
		if (meins == null) {
			if (other.meins != null)
				return false;
		} else if (!meins.equals(other.meins))
			return false;
		if (posId != other.posId)
			return false;
		if (theoric == null) {
			if (other.theoric != null)
				return false;
		} else if (!theoric.equals(other.theoric))
			return false;
		if (theoricCost == null) {
			if (other.theoricCost != null)
				return false;
		} else if (!theoricCost.equals(other.theoricCost))
			return false;
		if (transit == null) {
			if (other.transit != null)
				return false;
		} else if (!transit.equals(other.transit))
			return false;
		if (vhilm == null) {
			if (other.vhilm != null)
				return false;
		} else if (!vhilm.equals(other.vhilm))
			return false;
		if (vhilmCounted == null) {
			if (other.vhilmCounted != null)
				return false;
		} else if (!vhilmCounted.equals(other.vhilmCounted))
			return false;
		return true;
	}

}
