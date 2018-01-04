package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActivemq {

	@Test
	public void testActiveMQ() throws JMSException {
		// 1 创建一个ConnectionFactory工厂对象 需要制指定ip和端口号
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.229.6:61616");
		// 2 创建连接
		Connection connection = connectionFactory.createConnection();
		// 3 开启连接
		connection.start();
		// 4 创建会话
		// 第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
		// 第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 5 使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
		Queue queue = session.createQueue("test-queue");
		// 6 使用Session创建一个生产者producer
		MessageProducer producer = session.createProducer(queue);
		// 7 使用session创建一个message对象
		TextMessage textMessage = session
				.createTextMessage("hello activeMq,this is my first test.");
		// 8 使用producer发送信息
		producer.send(textMessage);
		// 9 关闭资源
		producer.close();
		session.close();
		connection.close();

	}

	@Test
	public void testActiveMQConsumer() throws Exception {
		// 1 创建一个connectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.229.6:61616");
		// 2 使用ConnectionFactory 创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 3 开启连接
		connection.start();
		// 4 使用connection 创建一个session
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 5 使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
		Queue queue = session.createQueue("test-queue");
		// 6 使用session创建consumer
		MessageConsumer consumer = session.createConsumer(queue);
		// 7 使用consumer 接收内容
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;

				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//等待键盘输入 为了防止程序走完
		System.in.read();
		// 第九步：关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
