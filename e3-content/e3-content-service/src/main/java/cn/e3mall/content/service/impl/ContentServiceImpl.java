package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容管理Service
 * <p>
 * Title: ContentServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.cn
 * </p>
 * 
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public E3Result addContent(TbContent content) {
		// 将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		// 插入到数据库
		contentMapper.insert(content);
		//缓存同步,删除缓存中对应的数据。
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	/**
	 * 根据内容分类id查询内容列表
	 * <p>
	 * Title: getContentListByCid
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param cid
	 * @return
	 * @see cn.e3mall.content.service.ContentService#getContentListByCid(long)
	 */
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查询缓存redis缓存 如果有数据直接返回缓存中的内容
		//异常捕获为了 不影响正常业务
		try {
			String json = jedisClient.hget(CONTENT_LIST, cid + "");
			if(StringUtils.isNoneBlank(json)){
				List<TbContent> jsonToList = JsonUtils.jsonToList(json, TbContent.class);
				return jsonToList;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		//把数据库内容 添加到redis缓存中
		try {
			
			jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(list).toString());
//			jedisClient.hset(CONTENT_LIST, cid + "", list.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public EasyUIDataGridResult getContentPageList(long categoryId,
			int page, int pageSize) {
		// 创建pojo对象 因为easyUI的 easyui-datagrid 分页查询需要返回json格式为
		/*{
			total:总数
			row:每一页中的数据
		}
		所以自定义一个符合的pojo对象 EasyUIDataGridResult
		*/
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		// 设置查询条件 因为这个插件已经在SqlMapConfig.xml 配置进去了
		// 也就是在执行sql语句前必须先配置好条件 在这里使用了ibatis的切面技术 也就是在配置好了这个插件之后每次执行
		// sql语句都会先执行插件的设置对sql语句进行修改后再执行
		PageHelper.startPage(page, pageSize);
		
		//查询pojo中的row
		TbContentExample example = new TbContentExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		easyUIDataGridResult.setRows(list);
		
		//查询pojo中的total值
		PageInfo info = new PageInfo<>(list);
		easyUIDataGridResult.setTotal(info.getTotal());
		
		return easyUIDataGridResult;
	}

}
