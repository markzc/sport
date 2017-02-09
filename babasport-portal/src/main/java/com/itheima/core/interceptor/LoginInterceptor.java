package com.itheima.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.itheima.common.utils.RequestUtils;
import com.itheima.core.service.SessionProvider;
/**
 * 拦截器  判断用户是否登陆
 * @author lx
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	
	@Autowired
	private SessionProvider sessionProvider;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		// 1、 判断用户是否登陆 1）未登陆 跳转到登陆页面 跳转到首页 2）登陆 放行  
		String username = sessionProvider.getAttibuterForUserName(RequestUtils.getCSessionID(request, response));
		if(null == username){
			//未登陆
			response.sendRedirect("http://localhost:8082/login.aspx?ReturnUrl=http://localhost:8081/");
			return false;
		}
		//放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
