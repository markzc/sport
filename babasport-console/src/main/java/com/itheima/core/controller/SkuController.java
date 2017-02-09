package com.itheima.core.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.product.Sku;
import com.itheima.core.service.product.SkuService;

/**
 * 库存管理
 * 去库存管理页面
 * 修改
 * 添加
 * @author lx
 *
 */
@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;
	//去库存列表
	@RequestMapping(value = "/sku/list.do")
	public String list(Long productId,Model model){
		List<Sku> skus = skuService.selectSkuListByProductId(productId);
		model.addAttribute("skus", skus);
		return "sku/list";
	}
	//保存
	@RequestMapping(value = "/sku/addSku.do")
	public void addSku(Sku sku,HttpServletResponse response){
		
		JSONObject jo = new JSONObject();
		//修改
		try {
			skuService.updateSkuById(sku);
			jo.put("message", "保存成功!");
		} catch (Exception e) {
			// TODO: handle exception
			jo.put("message", "保存失败!");
		}
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(jo.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
