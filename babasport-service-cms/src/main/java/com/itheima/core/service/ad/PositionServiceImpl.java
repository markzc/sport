package com.itheima.core.service.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.dao.ad.PositionDao;
import com.itheima.core.pojo.ad.Position;
import com.itheima.core.pojo.ad.PositionQuery;

/**
 * 广告位置管理
 * @author lx
 *
 */
@Service("positionService")
public class PositionServiceImpl implements PositionService {

	@Autowired
	private PositionDao positionDao;
	//PositionService
	//加载 父ID =0 的顶级元素
	public List<Position> selectPositionListByParentId(Long parentId){
		PositionQuery positionQuery = new PositionQuery();
		positionQuery.createCriteria().andParentIdEqualTo(parentId);
		return positionDao.selectByExample(positionQuery);
	}
	
}
