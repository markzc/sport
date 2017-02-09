package com.itheima.core.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itheima.common.utils.RequestUtils;
import com.itheima.core.pojo.user.Buyer;
import com.itheima.core.service.BuyerService;
import com.itheima.core.service.SessionProvider;

/**
 * 单点登陆系统
 * 去登陆页面
 * 提交登陆表单
 * 密码加密
 * @author lx
 *
 */
@Controller
public class LoginController {

	
	//去登陆页面
	@RequestMapping(value = "/login.aspx",method = RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	@Autowired
	private BuyerService buyerService;
	@Autowired
	private SessionProvider sessionProvider;
	//提交登陆
	@RequestMapping(value = "/login.aspx",method = RequestMethod.POST)
	public String login(String ReturnUrl,String username,String password,Model model
			,HttpServletRequest request,HttpServletResponse response){//Cookie 
		//1：用户名不能为空
		if(null != username){
			//2:密码不能为空
			if(null != password){
				//3:用户名必须正确 
				Buyer buyer = buyerService.selectBuyerByUserName(username);
				if(null != buyer){
					//4:密码必须正确
					if(buyer.getPassword().equals(encodePassword(password))){
						//
						//5:将用户名 Session
						sessionProvider.setAttibuterForUserName
						(RequestUtils.getCSessionID(request, response), username);
						//6:返回之前访问页面
						return "redirect:" + ReturnUrl;
					}else{
						model.addAttribute("error", "密码必须正确");
					}
					
				}else{
					model.addAttribute("error", "用户名必须正确");
				}
				
			}else{
				model.addAttribute("error", "密码不能为空");
			}
			
		}else{
			model.addAttribute("error", "用户名不能为空");
		}
		return "login";
	}
	
	//密码加密  
	public String encodePassword(String password){
		//MD5  
		String algorithm = "MD5";
		char[] encodeHex = null;
		//加盐
//		password = "saf++dsafqwf123456greryyufadstqwnhweg";
		
		try {
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			byte[] digest = instance.digest(password.getBytes());
			//十六制
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encodeHex);
	}
	public static void main(String[] args) {
		LoginController l = new LoginController();
		String s = l.encodePassword("123456");
		System.out.println(s);
	}
	
	//判断用户是否登陆
	@RequestMapping(value = "/isLogin.aspx")
	public @ResponseBody
	MappingJacksonValue isLogin(String callback,HttpServletRequest request,HttpServletResponse response){
		Integer  result = 0;
		String username = sessionProvider.getAttibuterForUserName(RequestUtils.getCSessionID(request, response));
		if(null != username){
			//登陆了
			result = 1;
		}
		//Spring提供一个类
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		//返回给哪个Jquery 
		mjv.setJsonpFunction(callback);
		return mjv ;
	}
	
}
