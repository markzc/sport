package com.itheima.core.controller;

import java.util.List;

import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.Color;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.service.product.BrandService;
import com.itheima.core.service.product.ColorService;
import com.itheima.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;

/**
 * 商品管理
 * 
 * @author lx
 *
 */
@Controller
public class ProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	// 列表
	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo, String name, Long brandId, Boolean isShow, Model model) {
		// 返回品牌结果集
		List<Brand> brands = brandService.selectBrandListByQuery(null, 1);
		model.addAttribute("brands", brands);
		// 返回分页对象
		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("isShow", isShow);
		
		return "product/list";
	}
	
	@Autowired
	private ColorService colorService;
	//去商品添加页面
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(Model model){
		
		//品牌结果集
		// 返回品牌结果集
		List<Brand> brands = brandService.selectBrandListByQuery(null, 1);
		model.addAttribute("brands", brands);
		//颜色结果集
		List<Color> colors = colorService.selectColorListByQuery();
		model.addAttribute("colors", colors);
		return "product/add";
		
	}
	//商品添加
	@RequestMapping(value = "/product/add.do")
	public String add(Product product){
		
		//保存商品    库存
		productService.insertProduct(product);
		
		return "redirect:/product/list.do";
	}
	
	//上架
	@RequestMapping(value = "/product/isShow.do")
	public String isShow(Long[] ids){
		//上架 批量
		productService.isShow(ids);
		
		return "forward:/product/list.do";
	}
	

}
