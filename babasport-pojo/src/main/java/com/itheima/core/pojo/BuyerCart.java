package com.itheima.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itheima.core.pojo.product.Sku;
/**
 * 购物车
 * @author lx
 *
 */
public class BuyerCart implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//结果集   
	private List<BuyerItem> items = new ArrayList<BuyerItem>();
	
	
	//加入购物项
	public void addItem(Long skuId,Integer amount){
		
		Sku sku = new Sku();
		sku.setId(skuId);
		//购物项一个
		BuyerItem item = new BuyerItem();
		item.setSku(sku);
		item.setAmount(amount);
		
		//判断是否同款
		if(items.contains(item)){
			//有同款
			for (BuyerItem it : items) {
				if(it.equals(item)){
					//数量追加
					it.setAmount(it.getAmount() + item.getAmount());
				}
			}
		}else{
			//追加购物项
			items.add(item);
		}
		
	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}
	
	
	//小计   
//	商品数量 
	@JsonIgnore
	public Integer getProductAmount(){
		Integer result = 0;
		for (BuyerItem item : items) {
			result += item.getAmount();
		}
		return result;
	}
//	 商品金额   
	@JsonIgnore
	public Float getProductPrice(){
		Float result = 0f;
		for (BuyerItem item : items) {
			result += item.getAmount()*item.getSku().getPrice();
		}
		return result;
	}
//	商品运费   
	@JsonIgnore
	public Float getFee(){
		Float result = 0f;
		if(getProductPrice() < 99){
			result = 6f;
		}
		return result;
	}
//	总计
	@JsonIgnore
	public Float getTotalPrice(){
		return getProductPrice() + getFee();
	}
	
}
