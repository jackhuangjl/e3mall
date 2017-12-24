package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentCatController {

	@Autowired
	ContentCategoryService categoryService;
	
	@RequestMapping("/content/category/list")
	public @ResponseBody List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id", defaultValue="0")Long parentId) {
		List<EasyUITreeNode> list = categoryService.getContentCatList(parentId);
		return list;
	}
}