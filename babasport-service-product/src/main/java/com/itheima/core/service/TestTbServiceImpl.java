package com.itheima.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.core.dao.TestTbDao;
import com.itheima.core.pojo.TestTb;

/**
 * 测试
 * @author lx
 *
 */
@Service("testTbService")
@Transactional
public class TestTbServiceImpl implements TestTbService{

	@Autowired
	private TestTbDao testTbDao;
	
	//保存

	public void insertTestTb(TestTb testTb){
		testTbDao.insertTestTb(testTb);
//		throw new RuntimeException();
	}
	
}
