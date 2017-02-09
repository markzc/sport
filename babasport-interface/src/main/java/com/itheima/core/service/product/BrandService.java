package com.itheima.core.service.product;

import java.util.List;

import com.itheima.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface BrandService {
	
	//查询结果集
	public List<Brand> selectBrandListByQuery(String name,Integer isDisplay);
	
	//返回分页对象
	public Pagination selectPaginationByQuery(Integer pageNo,String name, Integer isDisplay);
	
	//通过ID查询品牌
	public Brand selectBrandById(Long id);
	
	//修改
	public void updateBrandById(Brand brand);
	
	//批量删除
	public void deletes(Long[] ids);//入参： 直接传递数组或集群     <foreach collection=array或list

}
