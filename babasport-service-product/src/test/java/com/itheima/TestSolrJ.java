package com.itheima;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestSolrJ {
	
	
	@Autowired
	private CloudSolrServer cloudSolrServer;
	
	//集群版
	@Test
	public void testSolrCloud() throws Exception {
		
//		String zkHost = "192.168.200.128:2181,192.168.200.128:2182,192.168.200.128:2183";
//		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
		//设置  默认的总结合 
//		cloudSolrServer.setDefaultCollection("collection1");
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 6);
		doc.setField("name", "熊二");
		//保存
		cloudSolrServer.add(doc);
		cloudSolrServer.commit();
		
	}
	

	@Autowired
	private SolrServer solrServer;
	@Test
	public void testname() throws Exception {
/*		String baseURL = "http://192.168.200.128:8080/solr";
		SolrServer solrServer = new HttpSolrServer(baseURL);*/
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 2);
		doc.setField("name", "熊大");
		//保存
		solrServer.add(doc);
		solrServer.commit();
		
	}
	@Test
	public void testQuery() throws Exception {
		
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", "*:*");
		//查询
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList docs = response.getResults();
		long numFound = docs.getNumFound();
		System.out.println(numFound);
		
	}
}
