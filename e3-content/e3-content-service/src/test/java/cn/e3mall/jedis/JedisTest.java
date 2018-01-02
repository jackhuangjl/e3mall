package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testJedis() {
		// 创建一个连接Jedis对象，参数：host port
		Jedis jedis = new Jedis("192.168.229.3", 6379);
		// 直接使用jedis操作redis，也就是命令
		jedis.set("test123", "my first jedis test");
		String string = jedis.get("test123");
		System.out.println(string);
		jedis.close();
	}

	@Test
	public void testJedisPool() {
		// 创建一个连接池对象 两个参数为host port
		JedisPool jedisPool = new JedisPool("192.168.229.3", 6379);
		// 从连接池中获得一个连接，就是一个jedis对象
		Jedis jedis = jedisPool.getResource();
		// 使用jedis操作redis
		String string = jedis.get("test123");
		System.out.println("pool= " + string);
		// 关闭连接，每次使用完毕之后必须关闭连接，让连接池回收资源 不然就是把资源用完
		jedis.close();
		// 关闭连接池 因为这里只是一个测试的案例
		jedisPool.close();
	}

	@Test
	public void testJedisCluster() throws Exception {
		// 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.229.3", 7001));
		nodes.add(new HostAndPort("192.168.229.3", 7002));
		nodes.add(new HostAndPort("192.168.229.3", 7003));
		nodes.add(new HostAndPort("192.168.229.3", 7004));
		nodes.add(new HostAndPort("192.168.229.3", 7005));
		nodes.add(new HostAndPort("192.168.229.3", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		// 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
		jedisCluster.set("hello", "100");
		String result = jedisCluster.get("hello");
		// 第三步：打印结果
		System.out.println(result);
		// 第四步：系统关闭前，关闭JedisCluster对象。
		jedisCluster.close();
	}

}
