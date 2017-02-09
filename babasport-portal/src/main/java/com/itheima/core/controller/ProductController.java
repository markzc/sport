package com.itheima.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.common.utils.JsonUtils;
import com.itheima.core.pojo.ad.Ad;
import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.Color;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.pojo.user.Buyer;
import com.itheima.core.service.CmsService;
import com.itheima.core.service.SearchService;
import com.itheima.core.service.ad.AdService;

import cn.itcast.common.page.Pagination;

/**
 * 首页
 * 搜索
 * 详情
 * @author lx
 *
 */
@Controller
public class ProductController {

	
	@Autowired
	private AdService adService;
	
	//进入首页  http://localhost:8081
	@RequestMapping(value = "/")
	public String index(Model model) throws Exception{
		//大广告位置 上所有广告 
		model.addAttribute("ads", adService.selectAdListByPositionIdWithRedis(89L));
		return "index";
	}
	
	public static void main(String[] args) throws JsonProcessingException {
		
		Buyer buyer = new Buyer();
		buyer.setUsername("范冰冰");
		
		ObjectMapper om = new ObjectMapper();
		//不要NULl
		om.setSerializationInclusion(Include.NON_NULL);
		
		String s = om.writeValueAsString(buyer);
		System.out.println(s);
	}
	
	@Autowired
	private SearchService searchService;
	//搜索
	@RequestMapping(value = "/Search")
	public String search(Integer pageNo,String keyword,Long brandId,String price, Model model) throws Exception{
		//查询品牌结果集  从Redis服务器查询
		List<Brand> brands = searchService.selectBrandListFromRedis();
		model.addAttribute("brands", brands);
		//已选条件
		Map<String,String> map = new HashMap<String,String>();
		if(null != brandId){
			for (Brand brand : brands) {
				if(brand.getId().equals(brandId)){
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		//价格区间
		if(null != price){
			if(price.contains("-")){
				map.put("价格", price);
			}else{
				map.put("价格", price + "以上");
			}
		}
		//通过关键词查询SOlr服务器  排序  过滤  分页  
		Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword,brandId,price);
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		model.addAttribute("price", price);
		model.addAttribute("brandId", brandId);
		model.addAttribute("map", map);
		return "search";
	}
	
	@Autowired
	private CmsService cmsService;
	//去商品详情页面
	@RequestMapping(value = "/product/detail")
	public String detail(Long id,Model model){
		
		//通过商品ID 查询商品对象
		Product product = cmsService.selectProductById(id);
		//通过商品ID 查询SKu对象（里面有颜色对象）   库存大于0的
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		//去掉对象
		Set<Color> colors = new HashSet<Color>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		
		model.addAttribute("product", product);
		model.addAttribute("skus", skus);
		model.addAttribute("colors", colors);
		
		
		
		return "product";
	}
}
