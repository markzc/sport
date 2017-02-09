package com.itheima.core.service;

import java.util.List;

import com.itheima.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface SearchService {
	
	
	//通过关键词查询SOlr服务器  排序  过滤  分页  ;
	public Pagination selectPaginationByQuery(Integer pageNo,String keyword,Long brandId,String price) throws Exception;

	//查询品牌结果集  从Redis服务器查询
	public List<Brand> selectBrandListFromRedis();
	
	
	//保存商品信息到Solr服务器
	public void insertProductToSolr(Long id);
}
