package com.itheima.core.service.product;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;
import com.itheima.core.pojo.product.Color;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.pojo.product.ProductQuery;
import com.itheima.core.pojo.product.ProductQuery.Criteria;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.service.CmsService;
import com.itheima.core.service.StaticPageService;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	// 返回分页对象
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow){
		//创建商品条件对象
		ProductQuery productQuery = new ProductQuery();
		//当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数
		productQuery.setPageSize(5);
		
		Criteria createCriteria = productQuery.createCriteria();
		
		StringBuilder params = new StringBuilder();
		//判断
		if(null != name){
			createCriteria.andNameLike("%" + name + "%");
			params.append("name=").append(name);
		}
		if(null != brandId){
			createCriteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		//默认 isShow 下架  false
		if(null != isShow){
			createCriteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		}else{
			createCriteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		//分页对象
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				productDao.countByExample(productQuery)
				);
		//较正
		productQuery.setPageNo(pagination.getPageNo());
		//排序
		productQuery.setOrderByClause("id desc");
		//结果集
		List<Product> products = productDao.selectByExample(productQuery);
		pagination.setList(products);
		//分页在页面上展示
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}
	
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private Jedis jedis;
	//保存商品//保存商品    库存
	public void insertProduct(Product product){
		//商品ID
		Long id = jedis.incr("pno");
		product.setId(id);
		//默认下架
		product.setIsShow(false);
		//不删除
		product.setIsDel(true);
		//时间
		product.setCreateTime(new Date());
		
		//保存商品  insert into (id,name,....) values (1,hah,.null..)   保存数据库一样
		//保存商品  insert into (id,name) values (1,hah)
		productDao.insertSelective(product);
		
		//保存库存  商品ID  
		for(String colorId : product.getColors().split(",")){
			for(String size : product.getSizes().split(",")){
				Sku sku = new Sku();
				//商品ID
				sku.setProductId(product.getId());
				//颜色ID
				sku.setColorId(Long.parseLong(colorId));
				//尺码
				sku.setSize(size);
				//市场价
				sku.setMarketPrice(0f);
				//价格
				sku.setPrice(0f);
				//运费
				sku.setDeliveFee(10f);
				//购买限制
				sku.setUpperLimit(200);
				//库存
				sku.setStock(0);
				//时间
				sku.setCreateTime(new Date());
				//
				skuDao.insertSelective(sku);
			}
		}
		
	}
	@Autowired
//	private jdbcTemplate Spring 公司的 连接Mysql或Oracle  jdbc
	private JmsTemplate jmsTemplate;
	
	//上架 批量
	public void isShow(Long[] ids){
		
		Product product = new Product();
		product.setIsShow(true);
		
		for (final Long id : ids) {
			//改上架
			product.setId(id);
			productDao.updateByPrimaryKeySelective(product);
			//发送消息 给MQ
			jmsTemplate.send(new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createTextMessage(String.valueOf(id));
				}
			});

		}
		
		
	}
}
