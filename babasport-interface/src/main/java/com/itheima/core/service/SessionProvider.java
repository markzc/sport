package com.itheima.core.service;

public interface SessionProvider {
	
	//保存用户名 到Redis中
	//参数1 ： key 32位
	//参数2： 用户名
	public void setAttibuterForUserName(String key,String value);
	
	//获取用户名  从Redis中
	public String getAttibuterForUserName(String key);

}
