package com.itheima.core.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.common.utils.JsonUtils;
import com.itheima.common.utils.RequestUtils;
import com.itheima.core.pojo.BuyerCart;
import com.itheima.core.pojo.BuyerItem;
import com.itheima.core.pojo.order.Order;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.service.BuyerService;
import com.itheima.core.service.SessionProvider;

/**
 * 加入购物车 去购物车结算 结算 提交订单
 * 
 * @author lx
 *
 */
@Controller
public class CartController {

	@Autowired
	private SessionProvider sessionProvider;

	// 加入购物车
	@RequestMapping(value = "/shopping/buyerCart")
	public String buyerCart(Long skuId, Integer amount, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		BuyerCart buyerCart = null;
		// 1、获取Cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// BUYER_CART
				if ("BUYER_CART".equals(cookie.getName())) {
					// 2、获取Cookie中购物车
					buyerCart = JsonUtils.jsonToObject(cookie.getValue(), BuyerCart.class);
					// JSON字符串转对象 对象转JSON字符串
					break;
				}
			}
		}
		// 3、没有 创建购物车对象
		if (null == buyerCart) {
			buyerCart = new BuyerCart();
		}
		// 4、追加当前款
		if (null != skuId && null != amount) {
			buyerCart.addItem(skuId, amount);
		}
		// 判断用户是否登陆
		String username = sessionProvider.getAttibuterForUserName(RequestUtils.getCSessionID(request, response));
		if (null != username) {
			// 已登陆
			if (buyerCart.getItems().size() > 0) {
				// 5、将购物车中商品追加到Redis中
				buyerService.insertBuyerCartToRedis(buyerCart, username);
				// 6、清空Cookie
				Cookie cookie = new Cookie("BUYER_CART", null);
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		} else {
			// 未登陆
			if (null != skuId && null != amount) {
				// 5、创建Cookie 保存新购物车对象
				Cookie cookie = new Cookie("BUYER_CART", JsonUtils.objectToJson(buyerCart));
				// 存活时间
				cookie.setMaxAge(60 * 60 * 24);
				cookie.setPath("/");
				// 6、写回浏览器
				response.addCookie(cookie);
			}
		}

		// 7、重定向 或内部转发
		// return "forward:/shopping/toCart";
		return "redirect:/shopping/toCart";
	}

	@Autowired
	private BuyerService buyerService;

	// 去购物车结算
	@RequestMapping(value = "/shopping/toCart")
	public String toCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		BuyerCart buyerCart = null;
		// 1、获取Cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// BUYER_CART
				if ("BUYER_CART".equals(cookie.getName())) {
					// 2、获取Cookie中购物车
					buyerCart = JsonUtils.jsonToObject(cookie.getValue(), BuyerCart.class);
					// JSON字符串转对象 对象转JSON字符串
					break;
				}
			}
		}
		// 判断用户是否登陆
		String username = sessionProvider.getAttibuterForUserName(RequestUtils.getCSessionID(request, response));
		if (null != username) {
			// 已登陆
			// 3、有 追加到Redis中 清空Cookie
			if (null != buyerCart && buyerCart.getItems().size() > 0) {
				buyerService.insertBuyerCartToRedis(buyerCart, username);
				Cookie cookie = new Cookie("BUYER_CART", null);
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
			// 4、取出Redis中所有的购物车
			buyerCart = buyerService.selectBuyerCartFromRedis(username);
		}
		// 未登陆
		// 5、有
		if (null != buyerCart) {
			List<BuyerItem> items = buyerCart.getItems();
			if (items.size() > 0) {
				// 6、将购物车装满 skuId
				for (BuyerItem item : items) {
					item.setSku(buyerService.selectSkuById(item.getSku().getId()));
				}
			}
		}
		// 7、回显购物车对象
		model.addAttribute("buyerCart", buyerCart);
		// 8、跳转购物车页面
		return "cart";
	}

	// 去结算
	@RequestMapping(value = "/buyer/trueBuy")
	public String trueBuy(HttpServletRequest request,HttpServletResponse response,Model model) {
		// 2、 判断购物车中是否有商品 1）无商品 刷新购物车页面进行提示 2）有商品 继续判断
		String username = sessionProvider.getAttibuterForUserName(RequestUtils.getCSessionID(request, response));
		BuyerCart buyerCart = buyerService.selectBuyerCartFromRedis(username);
		List<BuyerItem> items = buyerCart.getItems();
		if(items.size() > 0){
			Boolean flag = false;
			//有
			// 3、 判断购物车中商品是否有货 1）无货 刷新购物车页面进行无货提示 2）有货 真过了  
			// 将购物车装满 skuId
			for (BuyerItem item : items) {
				item.setSku(buyerService.selectSkuById(item.getSku().getId()));
				//判断是否货  判断购买数据大于库存数量 无货
				if(item.getAmount() > item.getSku().getStock()){
					item.setIsHave(false);
					flag = true;
				}
			}
			//
			if(flag){
				//确实存在无货的商品
				model.addAttribute("buyerCart", buyerCart);
				return "cart";
			}
		}else{
			return "redirect:/shopping/toCart";
		}
		// 进入订单页面
		return "order";
	}
	//提交订单
	@RequestMapping(value = "/buyer/submitOrder")
	public String submitOrder(Order order,
			HttpServletRequest request,HttpServletResponse response){
		String username = sessionProvider.getAttibuterForUserName(RequestUtils.getCSessionID(request, response));
		//保存订单   订单详情
		buyerService.insertOrder(order, username);
		//Model 返回订单ID  价格  时间 
		
		return "success";
	}

}
