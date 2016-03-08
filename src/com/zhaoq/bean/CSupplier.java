package com.zhaoq.bean;

public class CSupplier {
	// 商家名称：cSupplier varchar(32),
	// 商家编号：cSupplierNo varchar(32),
	private String cSupplierNo;
	public CSupplier(String cSupplierNo, String cSupplier, String dDate) {
		super();
		this.cSupplierNo = cSupplierNo;
		this.cSupplier = cSupplier;
		this.dDate = dDate;
	}

	private String cSupplier;
	private String dDate;
	




	
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
