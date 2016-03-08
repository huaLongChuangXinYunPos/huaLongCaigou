package com.zhaoq.bean;

//任务编号：cCheckTaskNo varchar(32),任务名称：cCheckTask varchar(32),盘点日期:dCheckTask datetime]
public class t_CheckTast {
	private String cCheckTaskNo;
	@Override
	public String toString() {
		return cCheckTask;
	}
	private String cCheckTask;
	private String dCheckTask;
	public t_CheckTast(String cCheckTaskNo, String cCheckTask, String dCheckTask) {
		super();
		this.cCheckTaskNo = cCheckTaskNo;
		this.cCheckTask = cCheckTask;
		this.dCheckTask = dCheckTask;
	}

	
	public String getcCheckTaskNo() {
		return cCheckTaskNo;
	}
	public void setcCheckTaskNo(String cCheckTaskNo) {
		this.cCheckTaskNo = cCheckTaskNo;
	}
	public String getcCheckTask() {
		return cCheckTask;
	}
	public void setcCheckTask(String cCheckTask) {
		this.cCheckTask = cCheckTask;
	}
	public String getdCheckTask() {
		return dCheckTask;
	}
	public void setdCheckTask(String dCheckTask) {
		this.dCheckTask = dCheckTask;
	}


}
