package com.pulamsi.photomanager.bean;

//贴标签实体类
public class PasterLabel {
	private String id;
	private String labelName;// 标签名
	private String memberId;// 用户id 备用
	private Integer type;// 状态码 默认0代表普通 1代表热门

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
