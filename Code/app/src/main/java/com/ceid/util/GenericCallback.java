package com.ceid.util;

public interface GenericCallback<T>
{
	void onSuccess(T data);
	void onFailure(Exception e);
}
