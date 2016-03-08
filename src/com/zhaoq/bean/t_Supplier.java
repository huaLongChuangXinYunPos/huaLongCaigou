package com.zhaoq.bean;

//供应商t_Supplier【商家编号：cSupNo varchar(32),商家名称：cSupName varchar(64)】
public class t_Supplier {
	@Override
	public String toString() {
		return cSupNo + "---" + cSupName;
	}

	private String cSupNo;
	private String cSupName;

	public t_Supplier() {

	}

	public t_Supplier(String cSupNo, String cSupName) {

		this.cSupNo = cSupNo;
		this.cSupName = cSupName;
	}

	public String getcSupNo() {
		return cSupNo;
	}

	public void setcSupNo(String cSupNo) {
		this.cSupNo = cSupNo;
	}

	public String getcSupName() {
		return cSupName;
	}

	public void setcSupName(String cSupName) {
		this.cSupName = cSupName;
	}

}
