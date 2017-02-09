package com.itheima.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.ad.Position;
import com.itheima.core.service.ad.PositionService;

/**
 * 广告位置
 * @author lx
 *
 */
@Controller
public class PositionController {

	
	@Autowired
	private PositionService positionService;
	
	//加载广告位置树状结构   回调数据 JSOn字符串  改成回调页面（JSON格式的字符串）
	@RequestMapping(value = "/position/tree.do")
	public String tree(String root,Model model){
		List<Position> positions = null;
		//判断 30
		if("source".equals(root)){
			//加载 父ID =0 的顶级元素
			positions = positionService.selectPositionListByParentId(0L);
		}else{
			positions = positionService.
					selectPositionListByParentId(Long.parseLong(root));
			
		}
		model.addAttribute("list", positions);
		return "position/tree";
	}
	//加载广告位置 的子位置列表
	@RequestMapping(value = "/position/list.do")
	public String list(String root,Model model){
		
		List<Position> positions = positionService.
				selectPositionListByParentId(Long.parseLong(root));
		model.addAttribute("positions", positions);
		return "position/list";
	}
	
	
}
