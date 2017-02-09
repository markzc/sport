package com.itheima.core.dao.product;

import java.util.List;

import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.BrandQuery;

public interface BrandDao {
	
	//通过上面的二个条件 查询品牌结果集
	public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
	
//	总条数，
	public Integer countBrandByQuery(BrandQuery brandQuery);
//	结果集  //limit 开始行，每页数
//	public List<Brand> selectBrandListByQueryWithLimit(BrandQuery brandQuery);
	//通过ID查询品牌
	public Brand selectBrandById(Long id);
	
	//修改
	public void updateBrandById(Brand brand);
	//批量删除
	public void deletes(Long[] ids);//入参： 直接传递数组或集群     <foreach collection=array或list
//	public void deletes(QueryVo vo);//入参：QueryVo里面封装了(数组或集合）  <foreach collection=ids
	

}
