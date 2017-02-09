package com.itheima.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.TestTb;
import com.itheima.core.service.TestTbService;

/**
 * 后台管理中心  
 * @author lx
 *
 */
@Controller
@RequestMapping(value = "/control/")
public class CenterController {
	
	
	//入口
	@RequestMapping(value = "index.do")
	public String index(){
		
		return "index";
	}
	//入口  -头
	@RequestMapping(value = "top.do")
	public String top(){
		return "top";
	}
	//入口  -身体
	@RequestMapping(value = "main.do")
	public String main(){
		return "main";
	}
	//入口  -身体 --左
	@RequestMapping(value = "left.do")
	public String left(){
		return "left";
	}
	//入口  -身体 --右
	@RequestMapping(value = "right.do")
	public String right(){
		return "right";
	}
	//商品--身体
	@RequestMapping(value = "frame/product_main.do")
	public String product_main(){
		return "frame/product_main";
	}
	
	//商品--身体--左
	@RequestMapping(value = "frame/product_left.do")
	public String product_left(){
		return "frame/product_left";
	}
	//广告--身体
	@RequestMapping(value = "frame/ad_main.do")
	public String ad_main(){
		return "frame/ad_main";
	}
	
	//广告--身体--左
	@RequestMapping(value = "frame/ad_left.do")
	public String ad_left(){
		return "frame/ad_left";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*	@Autowired
	private TestTbService testTbService;
	//第一个测试程序
	@RequestMapping(value = "/test/index.do")
	public String index(){
		TestTb testTb = new TestTb();
		testTb.setName("吴昕");
		testTb.setBirthday(new Date());
		testTbService.insertTestTb(testTb);
		return "NewFile";
	}*/

}
