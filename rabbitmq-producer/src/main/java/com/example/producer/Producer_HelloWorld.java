package com.example.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_HelloWorld {
    public static void main(String[] args) throws IOException, TimeoutException {

        // 1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 2.设置参数
        factory.setHost("118.31.68.83"); // ip 默认值为localhost
        factory.setPort(5672); // 端口 默认值为5672
        factory.setVirtualHost("/"); // 虚拟机 默认值为/
        factory.setUsername("admin"); // 用户名 默认值为guest
        factory.setPassword("123456"); // 密码 默认值为guest

        // 3.创建连接Connection
        Connection connection = factory.newConnection();

        // 4.创建Channel
        Channel channel = connection.createChannel();

        // 5.创建队列
        /**
         * 参数：
         * 1.String queue： 队列名称
         * 2.boolean durable： 是否持久化（mq重启后是否保存）
         * 3.boolean exclusive：
         *      是否独占，只能有一个消费者监听队列
         *      Connection关闭时，是否删除队列
         * 4.boolean autoDelete：是否自动删除 当没有Consumer时，自动删除
         * 5.Map<String, Object> arguments
         */
        // 如果没有queue 则会创建
        String queueName = "hello_world";

        channel.queueDeclare(queueName, true, false, false, null);

        // 6.发送消息
        /**
         * 参数：
         * 1.String exchange：交换机名称。简单模式使用默认的""
         * 2.String routingKey：路由名称。
         * 3.BasicProperties props：配置信息
         * 4.byte[] body：发送的消息数据
         */
        String body = "hello, send a message";

        channel.basicPublish("", queueName, null, body.getBytes());

        // 7.释放资源
        channel.close();
        connection.close();
    }
}
