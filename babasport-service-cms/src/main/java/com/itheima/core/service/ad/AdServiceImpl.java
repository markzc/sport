package com.itheima.core.service.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.common.utils.JsonUtils;
import com.itheima.core.dao.ad.AdDao;
import com.itheima.core.dao.ad.PositionDao;
import com.itheima.core.pojo.ad.Ad;
import com.itheima.core.pojo.ad.AdQuery;

import redis.clients.jedis.Jedis;

/**
 * 广告管理
 * @author lx
 *
 */
@Service("adService")
public class AdServiceImpl implements AdService {

	
	@Autowired
	private AdDao adDao;
	@Autowired
	private PositionDao positionDao;
	
	//ROot 广告位置 ID  广告位置下所有广告加载
	public List<Ad> selectAdListByPositionId(Long id){
		AdQuery adQuery = new AdQuery();
		adQuery.createCriteria().andPositionIdEqualTo(id);
		List<Ad> ads = adDao.selectByExample(adQuery);
		for (Ad ad : ads) {
			ad.setPosition(positionDao.selectByPrimaryKey(ad.getPositionId()));
		}
		return ads;
	}
	@Autowired
	private Jedis jedis;
	//ROot 广告位置 ID  广告位置下所有广告加载
	public String selectAdListByPositionIdWithRedis(Long id){
		//从缓存中查询
		String ads = jedis.get("ads");
		if(null == ads){
			AdQuery adQuery = new AdQuery();
			adQuery.createCriteria().andPositionIdEqualTo(id);
			//放一份到缓存中
			ads = JsonUtils.objectToJson(adDao.selectByExample(adQuery));
			jedis.set("ads", ads);
			jedis.expire("ads", 60*60*24);
		}
		return ads;
	}
}
