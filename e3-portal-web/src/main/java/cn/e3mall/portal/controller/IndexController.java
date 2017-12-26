package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 首页展示Controller
 * <p>Title: IndexController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class IndexController {
	@Value(value="${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	
	@Autowired
	ContentService contentService;
	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		List<TbContent> list = contentService.getContentListByCid(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
