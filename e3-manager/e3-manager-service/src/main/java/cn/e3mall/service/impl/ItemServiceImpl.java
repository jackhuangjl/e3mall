package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	/**
	 * 跟住主键查询商品
	 */
	@Autowired
	private TbItemMapper itemMapper;
	public TbItem getItemById(long itemId) {
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		
		//根据条件查询
		TbItemExample example = new TbItemExample();
		//
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null)
			return list.get(0);
		return null;
	}
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//创建pojo对象
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		//设置查询条件 因为这个插件已经在SqlMapConfig.xml 配置进去了
		//也就是在执行sql语句前必须先配置好条件 在这里使用了ibatis的切面技术 也就是在配置好了这个插件之后每次执行
		//sql语句都会先执行插件的设置对sql语句进行修改后再执行
		PageHelper.startPage(page, rows);
		//创建查询对象
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//把查询到的所有的内容放入对象
		easyUIDataGridResult.setRows(list);
		//创建pagehelper信息对象，然后把查询到的所有数据传入该对象 然后按照设置的参数进行分页参数的计算和保存
		PageInfo pageInfo = new PageInfo(list);
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		return easyUIDataGridResult;
	}

}
