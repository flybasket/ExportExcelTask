package com.wisewater.framework.vo;

import java.util.List;

public interface QueryDataCallbackListener<T> {
	//
	public void queryDataCompleteCallback(List<T> list);
}
