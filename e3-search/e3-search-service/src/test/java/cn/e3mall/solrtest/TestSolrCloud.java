package cn.e3mall.solrtest;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	@Test
	public void testAddDocument() throws Exception {
		// create a cluster connection, new a cluster CloudSolrServer object
		CloudSolrServer solrServer = new CloudSolrServer(
				"192.168.229.4:2181,192.168.229.4:2182,192.168.229.4:2183");
		// zkHost:zookeeper address table
		// setting one defaultCollection
		solrServer.setDefaultCollection("collection2");

		// 创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		// 想文档中添加域 每个document必须有一个id域
		document.setField("id", "solrcloud01");
		document.setField("item_title", "测试商品01");
		document.setField("item_price", 123);
		// document对象写入索引库
		solrServer.add(document);
		// 提交索引库
		solrServer.commit();
	}
	@Test
	public void testQuery() throws Exception{
		// create a cluster connection, new a cluster CloudSolrServer object
		CloudSolrServer solrServer = new CloudSolrServer(
				"192.168.229.4:2181,192.168.229.4:2182,192.168.229.4:2183");
		// setting one defaultCollection
		solrServer.setDefaultCollection("collection2");
		//创建查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//获取查询结果
		SolrDocumentList results = queryResponse.getResults();
		System.out.println(results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("title"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
		
	}
}
