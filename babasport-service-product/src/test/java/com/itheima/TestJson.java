package com.itheima;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.core.dao.TestTbDao;
import com.itheima.core.pojo.TestTb;
import com.itheima.core.service.TestTbService;

/**
 * 测试
 * @author lx
 *
 */
public class TestJson {


	
	@Test
	public void testname() throws Exception {
		TestTb testTb = new TestTb();
		testTb.setName("谢娜");
		testTb.setBirthday(new Date());
		
		
		//转换器类
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		
		String value = om.writeValueAsString(testTb);
		System.out.println(value);
		
		
		
		
		//json转对象
		TestTb t = om.readValue(value, TestTb.class);
		System.out.println(t);
	
	}
}
