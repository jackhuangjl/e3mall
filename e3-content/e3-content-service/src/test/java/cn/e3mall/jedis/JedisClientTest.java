package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

public class JedisClientTest {
	
	@Test
	public void testJedisClient(){
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获取JedisClient对象 （因为JedisClient 是JedisClientPool和JedisClientCluster的父接口）
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("mytest", "jedisCient");
		String string = jedisClient.get("mytest");
		//因为生成的对象交给了spring容器管理了 而且默认是单例模式 所以不需要关闭
		System.out.println(string);
		
	}
}
