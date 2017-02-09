package com.itheima.core.service.ad;

import java.util.List;

import com.itheima.core.pojo.ad.Ad;

public interface AdService {
	
	//ROot 广告位置 ID  广告位置下所有广告加载
	public List<Ad> selectAdListByPositionId(Long id);
	
	//ROot 广告位置 ID  广告位置下所有广告加载
	public String selectAdListByPositionIdWithRedis(Long id);

}
