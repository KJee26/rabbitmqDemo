package com.example.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_HelloWorld {
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

        // 6.接受消息
        /**
         * 参数：
         * 1.String queue：接受队列名称
         * 2.boolean autoAck：是否自动确认
         * 3.CancelCallback cancelCallback：回调函数
         */
        String body = "hello, send a message";

        Consumer consumer = new DefaultConsumer(channel) {
            /**
             * 回调方法：当收到消息后，会自动执行该方法
             * 参数：
             * 1.consumerTag：标识
             * 2.envelope：获取交换机路由等信息
             * 3.BasicProperties：配置信息
             * 4.body：实际数据
             */


            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("consumerTag:" + consumerTag);
                System.out.println("Exchange:" + envelope.getExchange());
                System.out.println("RoutingKey:" + envelope.getRoutingKey());
                System.out.println("properties:" + properties);
                System.out.println("body:" + new String(body));

            }
        };
        channel.basicConsume(queueName, true, consumer);


        // 消费者无需关闭资源

    }
}
