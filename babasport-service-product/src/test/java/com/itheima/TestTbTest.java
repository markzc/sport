package com.itheima;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.core.dao.TestTbDao;
import com.itheima.core.pojo.TestTb;
import com.itheima.core.service.TestTbService;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestTbTest {

	
	@Autowired
//	private TestTbDao testTbDao;
	private TestTbService testTbService;
	
	@Test
	public void testname() throws Exception {
		TestTb testTb = new TestTb();
		testTb.setName("谢娜");
		testTb.setBirthday(new Date());
		
		testTbService.insertTestTb(testTb);
	
	}
}
