package cn.e3mall.solrtest;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class testSolr {
	
	@Test
	public void addDocument() throws Exception {
		// 1、把solrJ的jar包添加到工程。
		// 2、创建一个SolrServer对象。创建一个和sorl服务的连接。HttpSolrServer。
		//如果不带Collection默认连接Collection1
		SolrServer solrServer = new HttpSolrServer("http://192.168.229.4:8080/solr");
		// 3、创建一个文档对象。SolrInputDocument。
		SolrInputDocument document = new SolrInputDocument();
		// 4、向文档对象中添加域。必须有一个id域。而且文档中使用的域必须在schema.xml中定义。
		document.addField("id", "test001");
		document.addField("item_title", "测试商品");
		// 5、把文档添加到索引库
		solrServer.add(document);
		// 6、Commit。
		solrServer.commit();
	}
	@Test
	public void deleteDocumentById() throws Exception {
		// 第一步：创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.229.4:8080/solr");
		// 第二步：调用SolrServer对象的根据id删除的方法。
		solrServer.deleteById("test001");
		// 第三步：提交。
		solrServer.commit();
	}

}
