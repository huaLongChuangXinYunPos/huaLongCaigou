package com.zhaoq.bean;

//3、采购信息表WH_StockDetail[采购日期：dDate datetime,商家名称：cSupplier varchar(32),商家编号：cSupplierNo varchar(32),
//                      采购员姓名：cOperator varchar(32),采购员编号：cOperatorNo varchar(32),
//                      仓库编号：cWhNo varchar(32), 仓库名称：cWh  varchar(32),
//                      商品编号：cGoodsNo varchar(32),商品名称：cGoodsName varchar(64),
//                      商品条码：cBarcode varchar(32),采购数量：fQuantity money ,采购单价：fInPrice money,采购金额：fInMoney money]

public class WH_StockDetail {
	public WH_StockDetail() {
		super();
	}



	public WH_StockDetail(String dDate, String cSupplier, String cSupplierNo,
			String cOperator, String cOperatorNo, String cWhNo, String cWh,
			String cGoodsNo, String cGoodsName, String cBarcode,
			String fQuantity, String fInPrice, String fInMoney, String fQty_Cur) {
		super();
		this.dDate = dDate;
		this.cSupplier = cSupplier;
		this.cSupplierNo = cSupplierNo;
		this.cOperator = cOperator;
		this.cOperatorNo = cOperatorNo;
		this.cWhNo = cWhNo;
		this.cWh = cWh;
		this.cGoodsNo = cGoodsNo;
		this.cGoodsName = cGoodsName;
		this.cBarcode = cBarcode;
		this.fQuantity = fQuantity;
		this.fInPrice = fInPrice;
		this.fInMoney = fInMoney;
		this.fQty_Cur = fQty_Cur;
	}
	@Override
	public String toString() {
		return "WH_StockDetail [dDate=" + dDate + ", cSupplier=" + cSupplier
				+ ", cSupplierNo=" + cSupplierNo + ", cOperator=" + cOperator
				+ ", cOperatorNo=" + cOperatorNo + ", cWhNo=" + cWhNo
				+ ", cWh=" + cWh + ", cGoodsNo=" + cGoodsNo + ", cGoodsName="
				+ cGoodsName + ", cBarcode=" + cBarcode + ", fQuantity="
				+ fQuantity + ", fInPrice=" + fInPrice + ", fInMoney="
				+ fInMoney + ", fQty_Cur=" + fQty_Cur + "]";
	}
	private String dDate;
	private String cSupplier;
	private String cSupplierNo;
	private String cOperator;
	private String cOperatorNo;
	private String cWhNo;
	private String cWh;
	private String cGoodsNo;
	private String cGoodsName;
	private String cBarcode;
	private String fQuantity;
	private String fInPrice;
	private String fInMoney;
	private String fQty_Cur;
	
	private String fCKPrice;
	

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

	public String getcOperator() {
		return cOperator;
	}

	public void setcOperator(String cOperator) {
		this.cOperator = cOperator;
	}

	public String getcOperatorNo() {
		return cOperatorNo;
	}

	public void setcOperatorNo(String cOperatorNo) {
		this.cOperatorNo = cOperatorNo;
	}

	public String getcWhNo() {
		return cWhNo;
	}

	public void setcWhNo(String cWhNo) {
		this.cWhNo = cWhNo;
	}

	public String getcWh() {
		return cWh;
	}

	public void setcWh(String cWh) {
		this.cWh = cWh;
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

	public String getcBarcode() {
		return cBarcode;
	}

	public void setcBarcode(String cBarcode) {
		this.cBarcode = cBarcode;
	}

	public String getfQuantity() {
		return fQuantity;
	}

	public void setfQuantity(String fQuantity) {
		this.fQuantity = fQuantity;
	}

	public String getfInPrice() {
		return fInPrice;
	}

	public void setfInPrice(String fInPrice) {
		this.fInPrice = fInPrice;
	}

	public String getfInMoney() {
		return fInMoney;
	}

	public void setfInMoney(String fInMoney) {
		this.fInMoney = fInMoney;
	}

	public String getdDate() {
		return dDate;
	}

	public void setdDate(String dDate) {
		this.dDate = dDate;
	}

	public String getcSupplier() {
		return cSupplier;
	}

	public void setcSupplier(String cSupplier) {
		this.cSupplier = cSupplier;
	}

	public String getcSupplierNo() {
		return cSupplierNo;
	}

	public void setcSupplierNo(String cSupplierNo) {
		this.cSupplierNo = cSupplierNo;
	}

}
