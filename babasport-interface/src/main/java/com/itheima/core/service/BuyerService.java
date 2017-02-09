package com.itheima.core.service;

import com.itheima.core.pojo.BuyerCart;
import com.itheima.core.pojo.order.Order;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.pojo.user.Buyer;

public interface BuyerService {
	
	//通过用户名查询用户对象
	public Buyer selectBuyerByUserName(String username);
	
	
//	通过SKUID  查询SKU对象（商品ID查询商品对象，颜色ID查询颜色对象）
	public Sku selectSkuById(Long skuId);
	
	
	//将购物车保存到Redis中
	public void insertBuyerCartToRedis(BuyerCart buyerCart,String username);
	
	//获取购物车从Redis中
	public BuyerCart selectBuyerCartFromRedis(String username);
	
	//提交订单
	public void insertOrder(Order order,String username);

}
