package cn.e3mall.controller;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	//注入Jms模板接口
	@Autowired
	private JmsTemplate JmsTemplate;
	//注入Destination 因为spring配置文件中有两个bean是基于Destination接口
	//这两个bean就是queue和topic所以选用resource注解可以指定id来注入
	@Resource
	private Destination topicDestination;
	
	@RequestMapping("/item/{itemId}")
	public @ResponseBody TbItem getItemById(@PathVariable Long itemId){
		TbItem item = itemService.getItemById(itemId);
		return item;
	}	
	
	@RequestMapping("/item/list")
	public @ResponseBody EasyUIDataGridResult getItemList(Integer page, Integer rows){
		//调用服务查询商品列表
		return itemService.getItemList(page, rows);
	}
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		final E3Result result = itemService.addItem(item, desc);
		//在这里添加发送activeMQ 是因为 添加完新商品之后 需要等待事物的完成 方便导入新产品到索引库时 不会发生在mysql数据库中找不到对应的商品
		JmsTemplate.send(topicDestination,new MessageCreator() {		
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage textMessage = session.createTextMessage(result.getUuid() + "");
				return textMessage;
			}
		});	
		return result;
	}
}
