package com.itheima.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.dao.product.BrandDao;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;
import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.pojo.product.ProductQuery;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.pojo.product.SkuQuery;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

/**
 * 搜索
 * @author lx
 *
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	
	@Autowired
	private SolrServer solrServer;
	
	//通过关键词查询SOlr服务器  排序  过滤  分页  ;
	public Pagination selectPaginationByQuery(Integer pageNo,String keyword
			,Long brandId,String price) throws Exception{
		//创建
		ProductQuery productQuery = new ProductQuery();
		//当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数
		productQuery.setPageSize(8);
		
		SolrQuery solrQuery = new SolrQuery();
		//关键词
//		solrQuery.set("q", "name_ik:" + keyword);
		StringBuilder params = new StringBuilder();
		if(null != keyword){
			solrQuery.setQuery(keyword);
			//设置默认搜索域
			solrQuery.set("df", "name_ik");
			params.append("keyword=").append(keyword);
		}else{
			solrQuery.setQuery("*:*");
		}
		//查询指定的域
		solrQuery.set("fl", "id,name_ik,url,price");
		//过滤条件
		//品牌ID
		if(null != brandId){
			//过滤条件
//			solrQuery.set("fq", "brandId:" + brandId);
			solrQuery.addFilterQuery("brandId:" + brandId);
			params.append("&brandId=").append(brandId);
		}
		//价格区间   0-99 1600
		if(null != price){
			String[] p = price.split("-");
			if(p.length == 2){
				solrQuery.addFilterQuery("price:[" + p[0] + " TO " + p[1] + "]");//开区间   }  闭区间   ]
			}else{
				solrQuery.addFilterQuery("price:[" + p[0] + " TO *]");//开区间   }  闭区间   ]
			}
			params.append("&price=").append(price);
		}
		//高亮
		//设置高亮  3步
		//1：开启高亮
		solrQuery.setHighlight(true);
		//2:设置需要高亮的域
		solrQuery.addHighlightField("name_ik");
		//3:前缀 后缀
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		
		//排序 
		solrQuery.setSort("price", ORDER.asc);
		//分页  
		solrQuery.setStart(productQuery.getStartRow());
		//每页数
		solrQuery.setRows(productQuery.getPageSize());
		
		//执行查询
		QueryResponse response = solrServer.query(solrQuery);
		//4：取出高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		// 1:Map  K 商品id V Map
		// 2:Map  K name_ik     V List
		// 3:List<String>  list.get(0);
		
		//结果集
		SolrDocumentList docs = response.getResults();
	
		//总条数
		long numFound = docs.getNumFound();
		
		//创建分页对象
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				(int) numFound
				);
		
		//设置结果集
		List<Product> products = new ArrayList<Product>();
		for (SolrDocument doc : docs) {
			Product product = new Product();
			//商品ID
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			
			//商品名称
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
//			String name = (String) doc.get("name_ik");
			product.setName(list.get(0));
			//图片
			String url = (String) doc.get("url");
			product.setImgUrl(url);
			//价格
			product.setPrice((Float) doc.get("price"));
			
			products.add(product);
		}
		
		pagination.setList(products);
		
		//分页在页面上展示
		String url = "/Search";
		pagination.pageView(url, params.toString());
		return pagination;
		
		
	}
	@Autowired
	private Jedis jedis;
	@Autowired
	private BrandDao brandDao;
	//查询品牌结果集  从Redis服务器查询
	public List<Brand> selectBrandListFromRedis(){
		//1:从Redis查询   如果没有     Redis 保存品牌 缓存  时间 1分钟  
		Map<String, String> hgetALl = jedis.hgetAll("brand");
		if(null != hgetALl){
			Set<Entry<String, String>> entrySet = hgetALl.entrySet();
			if(null != entrySet && entrySet.size() >0){
				List<Brand> brands = new ArrayList<Brand>();
				for (Entry<String, String> entry : entrySet) {
					Brand brand = new Brand();
					brand.setId(Long.parseLong(entry.getKey()));
					brand.setName(entry.getValue());
					brands.add(brand);
				}
				//2:从Redis查询 有  直接返回
				return brands;
			}
		}
		//3:从Mysql查询   放进Redis中一份  返回结果集
		List<Brand> brands = brandDao.selectBrandListByQuery(null);
		for (Brand brand : brands) {
			jedis.hset("brand", "" + brand.getId(), brand.getName());
		}
		return brands;
	}
	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	
	//保存商品信息到Solr服务器
	public void insertProductToSolr(Long id){

		//TODO 保存商品信息到SOlr服务器  
		SolrInputDocument doc = new SolrInputDocument();
		//商品ＩＤ
		doc.setField("id", id);
		//商品名称
		Product p = productDao.selectByPrimaryKey(id);
		doc.setField("name_ik", p.getName());
		//图片
		doc.setField("url", p.getImgUrl());
		//价格  select price from bbs_sku where product_id = 442 order by price asc limit 0,1
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		//指定查询字段
		skuQuery.setFields("price");
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		doc.setField("price", skus.get(0).getPrice());
		//品牌ID 
		doc.setField("brandId", p.getBrandId());
		//时间
		try {
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
