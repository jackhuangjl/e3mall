package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
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
}
