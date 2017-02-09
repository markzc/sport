package com.itheima;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestRedis {

	
	@Autowired
	private JedisCluster jc;
	
	 //集群版
	@Test
	public void testRedisCluster() throws Exception {
	/*	Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		//六个
		nodes.add(new HostAndPort("192.168.200.128",6379));
		nodes.add(new HostAndPort("192.168.200.128",6380));
		nodes.add(new HostAndPort("192.168.200.128",6381));
		nodes.add(new HostAndPort("192.168.200.128",6382));
		nodes.add(new HostAndPort("192.168.200.128",6383));
		nodes.add(new HostAndPort("192.168.200.128",6384));
		JedisCluster jc = new JedisCluster(nodes);*/
		
		jc.set("haha", "33");
		String age = jc.get("haha");
		System.out.println(age);
		jc.close();
	}
	
	@Autowired
	private Jedis jedis;
	
	@Test
	public void testname() throws Exception {
		
//		Jedis jedis = new Jedis("192.168.200.128",6379);
		String age = jedis.get("zhangsan");
		System.out.println(age);
		
	
	}
}
