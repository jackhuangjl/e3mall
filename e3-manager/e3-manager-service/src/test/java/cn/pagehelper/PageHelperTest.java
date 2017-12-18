package cn.pagehelper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	@Test
	public void testPageHelper(){
		//��ʼ��spring����
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//��ȡMapper�������
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		//ִ��sql���֮ǰ���÷�ҳ��Ϣʹ��PageHelper��startPage����
		PageHelper.startPage(1, 10);
		//ִ�в�ѯ
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//ȡ��ҳ��Ϣ��PageInfo 1���ܼ�¼�� 2.��ҳ�� ��ǰҳ�����Ϣ
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(list.size());
	}
}
