package com.itheima.core.service.product;

import com.itheima.core.pojo.product.Product;

import cn.itcast.common.page.Pagination;

public interface ProductService {
	
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow);

	
	//保存商品//保存商品    库存
	public void insertProduct(Product product);
	
	//上架 批量
	public void isShow(Long[] ids);
}
