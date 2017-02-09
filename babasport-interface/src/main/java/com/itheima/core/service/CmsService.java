package com.itheima.core.service;

import java.util.List;

import com.itheima.core.pojo.product.Product;
import com.itheima.core.pojo.product.Sku;

public interface CmsService {
	
	
	//通过商品ID 查询商品对象
	public Product selectProductById(Long id);
	//通过商品ID 查询SKu对象结果集（里面有颜色对象）   库存大于0的
	public List<Sku> selectSkuListByProductId(Long id);

}
