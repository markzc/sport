package com.itheima.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.common.utils.JsonUtils;
import com.itheima.core.pojo.ad.Ad;
import com.itheima.core.service.ad.AdService;

/**
 * 广告管理
 * @author lx
 *
 */
@Controller
public class AdController {

	
	@Autowired
	private AdService adService;
	
	//广告位置下的所有广告管理
	@RequestMapping(value = "/ad/list.do")
	public String list(String root,Model model){
		//ROot 广告位置 ID  广告位置下所有广告加载
		List<Ad> ads = adService.selectAdListByPositionId(Long.parseLong(root));
		model.addAttribute("ads", ads);
		return "ad/list";
	}
}
