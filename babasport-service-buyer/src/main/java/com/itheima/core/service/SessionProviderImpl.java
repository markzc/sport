package com.itheima.core.service;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

/**
 * Session提供类  远程Session
 * @author lx
 *
 */
public class SessionProviderImpl implements SessionProvider{

	
	@Autowired
	private Jedis jedis;
	//分钟
	private Integer exp = 30;
	public void setExp(Integer exp) {
		this.exp = exp;
	}


	//保存用户名 到Redis中
	//参数1 ： key 32位
	//参数2： 用户名
	public void setAttibuterForUserName(String key,String value){
		//String
		jedis.set(key + ":USER_SESSION", value);
		//存活30分钟 默认
		jedis.expire(key + ":USER_SESSION", 60*exp);
	}
	//获取用户名  从Redis中
	public String getAttibuterForUserName(String key){
		String value = jedis.get(key + ":USER_SESSION");
		if(null != value){
			//存活30分钟 默认
			jedis.expire(key + ":USER_SESSION", 60*exp);
		}
		return value;
	}
	
	//保存验证码到Redis  Session 废弃了
	
	//获取验证码 从Redis
	
	//注销  删除Session
	
}
