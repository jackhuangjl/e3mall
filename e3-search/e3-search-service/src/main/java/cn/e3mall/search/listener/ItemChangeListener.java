package cn.e3mall.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.searchMapper.ItemMapper;

public class ItemChangeListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage) message;
		try {
			String uuid = textMessage.getText();
			long id = Long.parseLong(uuid);
			SearchItem searchItem = itemMapper.getItemById(id);
			
			//创建文档
			SolrInputDocument document = new SolrInputDocument();
			document.setField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			
			//添加文档到服务器
			solrServer.add(document);
			//提交到服务器
			solrServer.commit();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
