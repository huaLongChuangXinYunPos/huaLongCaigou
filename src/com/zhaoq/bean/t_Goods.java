package com.zhaoq.bean;
//2、商品基本信息表t_Goods【商品编号：cGoodsNo varchar(32),商品名称：cGoodsName varchar(64),
public class t_Goods {
	private String cGoodsNo;
	private String cGoodsName;
	private String cBarcode;
	private String cUnit;
	private String cSpec;
	private String fNormalPrice;
	private String fCKPrice;
	private String cSupNo;
	private String fQty_Cur;
	
	public t_Goods(String cGoodsNo, String cGoodsName, String cBarcode,
			String cUnit, String cSpec, String fNormalPrice, String fCKPrice,
			String cSupNo, String fQty_Cur) {
		super();
		this.cGoodsNo = cGoodsNo;
		this.cGoodsName = cGoodsName;
		this.cBarcode = cBarcode;
		this.cUnit = cUnit;
		this.cSpec = cSpec;
		this.fNormalPrice = fNormalPrice;
		this.fCKPrice = fCKPrice;
		this.cSupNo = cSupNo;
		this.fQty_Cur = fQty_Cur;
	}

	public String getfCKPrice() {
		return fCKPrice;
	}
	public void setfCKPrice(String fCKPrice) {
		this.fCKPrice = fCKPrice;
	}
	public String getfQty_Cur() {
		return fQty_Cur;
	}
	public void setfQty_Cur(String fQty_Cur) {
		this.fQty_Cur = fQty_Cur;
	}
	public String getcSupNo() {
		return cSupNo;
	}
	public void setcSupNo(String cSupNo) {
		this.cSupNo = cSupNo;
	}
	public String getcBarcode() {
		return cBarcode;
	}
	public void setcBarcode(String cBarcode) {
		this.cBarcode = cBarcode;
	}
	public String getcUnit() {
		return cUnit;
	}
	public void setcUnit(String cUnit) {
		this.cUnit = cUnit;
	}
	public String getcSpec() {
		return cSpec;
	}
	public void setcSpec(String cSpec) {
		this.cSpec = cSpec;
	}
	public String getfNormalPrice() {
		return fNormalPrice;
	}
	public void setfNormalPrice(String fNormalPrice) {
		this.fNormalPrice = fNormalPrice;
	}
	public String getfCkPrice() {
		return fCKPrice;
	}
	public void setfCkPrice(String fCKPrice) {
		this.fCKPrice = fCKPrice;
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
}
