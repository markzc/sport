package com.itheima.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.core.dao.product.BrandDao;
import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.BrandQuery;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {

	
	@Autowired
	private BrandDao brandDao;
	@Override
	public List<Brand> selectBrandListByQuery(String name, Integer isDisplay) {
		// TODO Auto-generated method stub
		BrandQuery brandQuery = new BrandQuery();
		if(null != name){
			brandQuery.setName(name);
		}
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
		}else{
			//  是 1 否 0     
			brandQuery.setIsDisplay(1);
		}
		return brandDao.selectBrandListByQuery(brandQuery);  //所有
	}
	//返回分页对象
	public Pagination selectPaginationByQuery(Integer pageNo,String name, Integer isDisplay){
		BrandQuery brandQuery = new BrandQuery();
		//当前页
		brandQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数 显示 2
		brandQuery.setPageSize(2);
		//品牌名称不为空
		StringBuilder params = new StringBuilder();
		if(null != name){
			brandQuery.setName(name);
			params.append("name=").append(name);
		}
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
			params.append("&isDisplay=").append(isDisplay);
		}else{
			//  是 1 否 0     
			brandQuery.setIsDisplay(1);
			params.append("&isDisplay=").append(1);
		}
		//创建分页对象
		Pagination pagination = new Pagination(
				brandQuery.getPageNo(),//总页数只有3页
				brandQuery.getPageSize(),
				brandDao.countBrandByQuery(brandQuery)
				);
		//较正当前页
		brandQuery.setPageNo(pagination.getPageNo());
		pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
		//分页在页面上的展示
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}
	
	//通过ID查询品牌
	public Brand selectBrandById(Long id){
		return brandDao.selectBrandById(id);
	}
	
	@Autowired
	private Jedis jedis;
	//Spring  ------  切面   通过Druid   连接Mysql        向发begin transaction      MYsql 开启事务                                                                                                                         数据库 Mysql 不支持事务
	//修改
	public void updateBrandById(Brand brand){
		//修改Mysql    Mybatis -- 通过Druid   连接Mysql  向发  update ......          保存Mysql中 但不显示
		brandDao.updateBrandById(brand);
		//修改Redis  hset  有 就是修改  没有就是添加
		jedis.hset("brand", "" + brand.getId(), brand.getName());
		jedis.expire("brand", 60);
	}
	//Spring  ------  切面    通过Druid   连接Mysql    向发 commit   callback
	
	//批量删除
	public void deletes(Long[] ids){
		brandDao.deletes(ids);
	}

}
