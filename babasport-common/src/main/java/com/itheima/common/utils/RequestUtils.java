package com.itheima.common.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取Cookie中的JSESSIONID
 * @author lx
 *
 */
public class RequestUtils {

	
	//获取Cookie中的JSESSIONID
	public static String getCSessionID(HttpServletRequest request,HttpServletResponse response){
		//1:取Cookie
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length > 0){
			//2:从Cookie中取令牌 
			for (Cookie cookie : cookies) {
				if("CSESSIONID".equals(cookie.getName())){
					//3:有  直接使用
					return cookie.getValue();
				}
			}
		}
		//4:没有  创建UUID 生成一个并去四个横线  32位   
		String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
		//5:保存此CSESSIONID 到Cookie中一份 
		Cookie cookie  = new Cookie("CSESSIONID",csessionid);
		//存活时间   -1 关闭浏览器销毁     立即销毁  0    时间 >0  60*60*24*7
		cookie.setMaxAge(-1);
		//设置路径    http://localhost:8881/portal/product.html    /portal
		//设置路径    http://localhost:8881/shopping/product.html    /shopping
		cookie.setPath("/");
		//6：回写到浏览器
		response.addCookie(cookie);
		//7:使用创建好的CSESSIONID
		return csessionid;
	}
}
