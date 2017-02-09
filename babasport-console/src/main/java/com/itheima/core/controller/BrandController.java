package com.itheima.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.product.Brand;
import com.itheima.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;

/**
 * 品牌管理
 * 列表查询
 * 修改
 * 添加
 * 删除（批量）
 * @author lx
 *
 */
@Controller
public class BrandController {

	
	@Autowired
	private BrandService brandService;
	//列表查询
	@RequestMapping(value = "/brand/list.do")
	public String list(Integer pageNo,String name,Integer isDisplay,Model model){
		
		
		//通过上面的二个条件 查询品牌结果集   日期 
//		List<Brand> brands = brandService.selectBrandListByQuery(name, isDisplay);
		//返回分页对象
		Pagination pagination = brandService.selectPaginationByQuery(pageNo, name, isDisplay);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		
		return "brand/list";
	}
	//去修改页面
	@RequestMapping(value = "/brand/toEdit.do")
	public String toEdit(Long id,Model model){
		
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		
		return "brand/edit";
	}
	//修改 提交
	@RequestMapping(value = "/brand/edit.do")
	public String edit(Brand brand){
		//修改
		brandService.updateBrandById(brand);
		return "redirect:/brand/list.do";
	}
	//删除（批量）
	@RequestMapping(value = "/brand/deletes.do")
	public String deletes(Long[] ids){
		//删除
		brandService.deletes(ids);
//		model.addAttribute("name", name);
//		model.addAttribute("isDisplay", isDisplay);
//		model.addAttribute("pageNo", pageNo);
//		return "redirect:/brand/list.do";
		return "forward:/brand/list.do";
	}
	
}
