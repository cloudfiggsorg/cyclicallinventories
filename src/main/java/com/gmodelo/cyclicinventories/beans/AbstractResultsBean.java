package com.gmodelo.cyclicinventories.beans;

import javax.xml.bind.annotation.XmlRootElement;

import com.gmodelo.cyclicinventories.utils.ReturnValues;

@XmlRootElement
public class AbstractResultsBean {

	private Integer resultId;
	private String resultMsgGen;
	private String resultMsgAbs;
	private String resultMsgCom;
	private Float floatResult;
	private Double doubleResult;
	private Boolean booleanResult;
	private Integer intCom1;
	private Integer intCom2;
	private Integer intCom3;
	private String strCom1;
	private String strCom2;
	private String strCom3;

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getResultMsgGen() {
		return resultMsgGen;
	}

	public void setResultMsgGen(String resultMsgGen) {
		this.resultMsgGen = resultMsgGen;
	}

	public String getResultMsgAbs() {
		return resultMsgAbs;
	}

	public void setResultMsgAbs(String resultMsgAbs) {
		this.resultMsgAbs = resultMsgAbs;
	}

	public String getResultMsgCom() {
		return resultMsgCom;
	}

	public void setResultMsgCom(String resultMsgCom) {
		this.resultMsgCom = resultMsgCom;
	}

	public Float getFloatResult() {
		return floatResult;
	}

	public void setFloatResult(Float floatResult) {
		this.floatResult = floatResult;
	}

	public Double getDoubleResult() {
		return doubleResult;
	}

	public void setDoubleResult(Double doubleResult) {
		this.doubleResult = doubleResult;
	}

	public Integer getIntCom1() {
		return intCom1;
	}

	public void setIntCom1(Integer intCom1) {
		this.intCom1 = intCom1;
	}

	public Integer getIntCom2() {
		return intCom2;
	}

	public void setIntCom2(Integer intCom2) {
		this.intCom2 = intCom2;
	}

	public Integer getIntCom3() {
		return intCom3;
	}

	public void setIntCom3(Integer intCom3) {
		this.intCom3 = intCom3;
	}

	public String getStrCom1() {
		return strCom1;
	}

	public void setStrCom1(String strCom1) {
		this.strCom1 = strCom1;
	}

	public String getStrCom2() {
		return strCom2;
	}

	public void setStrCom2(String strCom2) {
		this.strCom2 = strCom2;
	}

	public String getStrCom3() {
		return strCom3;
	}

	public void setStrCom3(String strCom3) {
		this.strCom3 = strCom3;
	}

	public Boolean getBooleanResult() {
		return booleanResult;
	}

	public void setBooleanResult(Boolean booleanResult) {
		this.booleanResult = booleanResult;
	}

	public AbstractResultsBean(Integer resultId, String resultMsgAbs) {
		super();
		this.resultId = resultId;
		this.resultMsgAbs = resultMsgAbs;
	}

	@Override
	public String toString() {
		return "AbstractResults [resultId=" + resultId + ", resultMsgGen=" + resultMsgGen + ", resultMsgAbs="
				+ resultMsgAbs + ", resultMsgCom=" + resultMsgCom + ", floatResult=" + floatResult + ", doubleResult="
				+ doubleResult + ", booleanResult=" + booleanResult + ", intCom1=" + intCom1 + ", intCom2=" + intCom2
				+ ", intCom3=" + intCom3 + ", strCom1=" + strCom1 + ", strCom2=" + strCom2 + ", strCom3=" + strCom3
				+ "]";
	}

	public AbstractResultsBean() {
		
		super();
		this.resultId = ReturnValues.ISUCCESS;		
		// TODO Auto-generated constructor stub
	}

	public AbstractResultsBean(Integer resultId, String resultMsgGen, String resultMsgAbs, String resultMsgCom,
			Float floatResult, Double doubleResult, Boolean booleanResult, Integer intCom1, Integer intCom2,
			Integer intCom3, String strCom1, String strCom2, String strCom3) {
		super();
		this.resultId = resultId;
		this.resultMsgGen = resultMsgGen;
		this.resultMsgAbs = resultMsgAbs;
		this.resultMsgCom = resultMsgCom;
		this.floatResult = floatResult;
		this.doubleResult = doubleResult;
		this.booleanResult = booleanResult;
		this.intCom1 = intCom1;
		this.intCom2 = intCom2;
		this.intCom3 = intCom3;
		this.strCom1 = strCom1;
		this.strCom2 = strCom2;
		this.strCom3 = strCom3;
	}

}
