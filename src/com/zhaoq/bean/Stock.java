package com.zhaoq.bean;

public class Stock {
public String getcGoodsNo() {
		return cGoodsNo;
	}

	public void setcGoodsNo(String cGoodsNo) {
		this.cGoodsNo = cGoodsNo;
	}

	public String getEndQty() {
		return EndQty;
	}

	public void setEndQty(String endQty) {
		EndQty = endQty;
	}

	public String getcGoodsName() {
		return cGoodsName;
	}

	public void setcGoodsName(String cGoodsName) {
		this.cGoodsName = cGoodsName;
	}

	public String getcBarcode() {
		return cBarcode;
	}

	public void setcBarcode(String cBarcode) {
		this.cBarcode = cBarcode;
	}

	public String getcSpec() {
		return cSpec;
	}

	public void setcSpec(String cSpec) {
		this.cSpec = cSpec;
	}

	public String getcUnit() {
		return cUnit;
	}

	public void setcUnit(String cUnit) {
		this.cUnit = cUnit;
	}

	//	cGoodsNo":"11499","EndQty":"10.0000","cGoodsName":"幸福未来10卷实心短卷柔绵感系列1500g"," +
//			""cBarcode":"6921744700053","cSpec":"1000g","cUnit":"提"}]}
	public Stock() {
		// TODO Auto-generated constructor stub
	}
	
	public Stock(String cGoodsNo, String endQty, String cGoodsName,
		String cBarcode, String cSpec, String cUnit) {
	super();
	this.cGoodsNo = cGoodsNo;
	EndQty = endQty;
	this.cGoodsName = cGoodsName;
	this.cBarcode = cBarcode;
	this.cSpec = cSpec;
	this.cUnit = cUnit;
}

	private String cGoodsNo;
	private String EndQty;
	private String cGoodsName;
	private String cBarcode;
	private String cSpec;
	private String cUnit;
	
	

}
