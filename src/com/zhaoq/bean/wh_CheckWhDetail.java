package com.zhaoq.bean;

//6、盘点子单商品表wh_CheckWhDetail【商品编号：cGoodsNo varchar(32),
//商品名称：cGoodsName varchar(64),盘点数量：fQuantity money】
public class wh_CheckWhDetail {

	public wh_CheckWhDetail() {
		super();
	}
	public wh_CheckWhDetail(String cGoodsNo, String cGoodsName,
			String cBarcode, String fQuantity, String fNormalPrice,
			String cCheckTaskNo, String cZoneNo, String cOperatorNo,
			String cOperator) {
		super();
		this.cGoodsNo = cGoodsNo;
		this.cGoodsName = cGoodsName;
		this.cBarcode = cBarcode;
		this.fQuantity = fQuantity;
		this.fNormalPrice = fNormalPrice;
		this.cCheckTaskNo = cCheckTaskNo;
		this.cZoneNo = cZoneNo;
		this.cOperatorNo = cOperatorNo;
		this.cOperator = cOperator;
	}
	private String cGoodsNo;
	private String cGoodsName;
	private String cBarcode;
	private String fQuantity;
	private String fNormalPrice;
	private String cCheckTaskNo;
	private String cZoneNo;
	private String cOperatorNo;
    private String cOperator;
    public String getcOperatorNo() {
		return cOperatorNo;
	}

	public void setcOperatorNo(String cOperatorNo) {
		this.cOperatorNo = cOperatorNo;
	}

	public String getcOperator() {
		return cOperator;
	}

	public void setcOperator(String cOperator) {
		this.cOperator = cOperator;
	}


	
	public String getcCheckTaskNo() {
		return cCheckTaskNo;
	}

	public void setcCheckTaskNo(String cCheckTaskNo) {
		this.cCheckTaskNo = cCheckTaskNo;
	}

	public String getcZoneNo() {
		return cZoneNo;
	}

	public void setcZoneNo(String cZoneNo) {
		this.cZoneNo = cZoneNo;
	}
	public String getfNormalPrice() {
		return fNormalPrice;
	}

	public void setfNormalPrice(String fNormalPrice) {
		this.fNormalPrice = fNormalPrice;
	}

	public String getcBarcode() {
		return cBarcode;
	}

	public void setcBarcode(String cBarcode) {
		this.cBarcode = cBarcode;
	}

	public String getcGoodsNo() {
		return cGoodsNo;
	}

	public void setcGoodsNo(String cGoodsNo) {
		this.cGoodsNo = cGoodsNo;
	}

	public String getcGoodsName() {
		return cGoodsName;
	}

	public void setcGoodsName(String cGoodsName) {
		this.cGoodsName = cGoodsName;
	}

	public String getfQuantity() {
		return fQuantity;
	}

	public void setfQuantity(String fQuantity) {
		this.fQuantity = fQuantity;
	}

}
