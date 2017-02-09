package com.itheima.core.service.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.itheima.core.service.SearchService;
/**
 * 自定义的监听器类
 * @author lx
 *
 */
public class CustomMessageListener implements MessageListener{

	
	@Autowired
	private SearchService searchService;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage atm = (ActiveMQTextMessage)message;
		//ID
		try {
			String id = atm.getText();
			System.out.println("SOlr项目中:" + id);
			//保存商品信息到Solr服务器
			searchService.insertProductToSolr(Long.parseLong(id));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
