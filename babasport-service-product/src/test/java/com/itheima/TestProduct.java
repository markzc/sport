package com.itheima;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.core.dao.TestTbDao;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.pojo.TestTb;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.service.TestTbService;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestProduct {

	
	@Autowired
	private ProductDao productDao;
	
	@Test
	public void testname() throws Exception {
		
	
		Product product = productDao.selectByPrimaryKey(3L);
		System.out.println(product);
	}
}
