package com.itheima.core.service.ad;

import java.util.List;

import com.itheima.core.pojo.ad.Position;

public interface PositionService {
	
	//加载 父ID =0 的顶级元素
	public List<Position> selectPositionListByParentId(Long parentId);

}
