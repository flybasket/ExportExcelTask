package com.wisewater.framework.vo;

public class QueryDataItemVo<T, R> {

	private T type;

	private R data;

	public T getType() {
		return type;
	}

	public R getData() {
		return data;
	}

	public void setType(T type) {
		this.type = type;
	}

	public void setData(R data) {
		this.data = data;
	}

	public void destory() {
		if (type != null)
			type = null;
		if (data != null)
			data = null;
	}

}
