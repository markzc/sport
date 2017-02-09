package com.itheima.core.service.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.itheima.core.pojo.product.Color;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.service.CmsService;
import com.itheima.core.service.StaticPageService;
/**
 * 自定义的监听器类
 * @author lx
 *
 */
public class CustomMessageListener implements MessageListener{


	@Autowired
	private CmsService cmsService;
	@Autowired
	private StaticPageService staticPageService;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage atm = (ActiveMQTextMessage)message;
		//ID
		try {
			String id = atm.getText();
			System.out.println("CMS项目中:" + id);
			//静态化 
			//查询数据
			Map<String,Object> root = new HashMap<String,Object>();
			//
			//通过商品ID 查询商品对象
			Product p = cmsService.selectProductById(Long.parseLong(id));
			//通过商品ID 查询SKu对象（里面有颜色对象）   库存大于0的
			List<Sku> skus = cmsService.selectSkuListByProductId(Long.parseLong(id));
			//去掉对象
			Set<Color> colors = new HashSet<Color>();
			for (Sku sku : skus) {
				colors.add(sku.getColor());
			}
			
			root.put("product", p);
			root.put("skus", skus);
			root.put("colors", colors);
			//调模板
			staticPageService.index(root, String.valueOf(id));

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
