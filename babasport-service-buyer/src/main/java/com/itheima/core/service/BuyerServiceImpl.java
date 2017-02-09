package com.itheima.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.dao.order.DetailDao;
import com.itheima.core.dao.order.OrderDao;
import com.itheima.core.dao.product.ColorDao;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;
import com.itheima.core.dao.user.BuyerDao;
import com.itheima.core.pojo.BuyerCart;
import com.itheima.core.pojo.BuyerItem;
import com.itheima.core.pojo.order.Detail;
import com.itheima.core.pojo.order.Order;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.pojo.user.Buyer;

import redis.clients.jedis.Jedis;

/**
 * 用户管理
 * @author lx
 *
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{

	
	@Autowired
	private BuyerDao buyerDao;
	@Autowired
	private Jedis jedis;
	
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ColorDao colorDao;
	
	//通过用户名查询用户对象
	public Buyer selectBuyerByUserName(String username){
//		从Redis  通过用户名 找到用户ID 
		String id = jedis.get(username);
		//声明
		Buyer buyer = null;
		if(null != id){
			//必有此用户
			buyer = buyerDao.selectByPrimaryKey(Long.parseLong(id));
		}
		return buyer;
	}
//	通过SKUID  查询SKU对象（商品ID查询商品对象，颜色ID查询颜色对象）
	public Sku selectSkuById(Long skuId){
		
		Sku sku = skuDao.selectByPrimaryKey(skuId);
		//
		sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
		
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		
		return sku;
	}
	//将购物车保存到Redis中
	public void insertBuyerCartToRedis(BuyerCart buyerCart,String username){
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem item : items) {
			jedis.hincrBy("buyerCart:" + username, "" + item.getSku().getId(), item.getAmount());
		}
	}
	
	//获取购物车从Redis中
	public BuyerCart selectBuyerCartFromRedis(String username){
		BuyerCart buyerCart = new BuyerCart();
		Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
		if(null != hgetAll){
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			if(null != entrySet && entrySet.size() > 0){
				for (Entry<String, String> entry : entrySet) {
					buyerCart.addItem(Long.parseLong(entry.getKey()),
							Integer.parseInt(entry.getValue()));
				}
			}
		}
		return buyerCart;
	}
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private DetailDao detailDao;
	//提交订单
	public void insertOrder(Order order,String username){
		//保存订单  
//		订单表
//		ID、订单编号   由Redis生成
		Long id = jedis.incr("oid");
		order.setId(id);
		
		BuyerCart buyerCart = selectBuyerCartFromRedis(username);
		// 将购物车装满 skuId
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem item : items) {
			item.setSku(selectSkuById(item.getSku().getId()));
		}
//		运费       由购物车提供
		order.setDeliverFee(buyerCart.getFee());
//		总价
		order.setTotalPrice(buyerCart.getTotalPrice());
//		订单价
		order.setOrderPrice(buyerCart.getProductPrice());
//		支付方式    由页面传递
//		支付要求    
//		留言       
//		送货方式   略
//		电话确认   略
//		支付状态   :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
		if(order.getPaymentWay() == 1){
			order.setIsPaiy(0);
		}else{
			order.setIsPaiy(1);
		}
//		订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
		order.setOrderState(0);
//		时间：     由程序提供
		order.setCreateDate(new Date());
//		用户ID   由程序提供
		String buyerId = jedis.get(username);
		order.setBuyerId(Long.parseLong(buyerId));
		
		//保存
		orderDao.insertSelective(order);
		
		
		//		 订单详情  多个
		for (BuyerItem item : items) {
	//		订单详情表
	//		Id   自增长
	//		订单ID  
			Detail detail = new Detail();
			detail.setOrderId(order.getId());
	//		商品ID（编号）   由购物车中购物项
			detail.setProductId(item.getSku().getProductId());
	//		商品名称
			detail.setProductName(item.getSku().getProduct().getName());
	//		颜色名称
			detail.setColor(item.getSku().getColor().getName());
	//		尺码名称
			detail.setSize(item.getSku().getSize());
	//		价格
			detail.setPrice(item.getSku().getPrice());
	//		数量、    快照
			detail.setAmount(item.getAmount());
			detailDao.insertSelective(detail);
		}
		//删除购物车
		jedis.del("buyerCart:" + username);
//		jedis.hdel("buyerCart:" + username, "5511","5566");
		
	}

	
}
