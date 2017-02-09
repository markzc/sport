package com.itheima.core.service.product;

import java.util.List;

import com.itheima.core.pojo.product.Sku;

public interface SkuService {

	// 通过商品ID查询Sku结果集
	public List<Sku> selectSkuListByProductId(Long productId);

	// 修改
	public void updateSkuById(Sku sku);
}
