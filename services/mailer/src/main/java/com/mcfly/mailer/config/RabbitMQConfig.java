package com.mcfly.mailer.config;

import com.mcfly.mailer.listeners.EmailConfirmationListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queues.emailConfirmQueue.name}")
    private String emailConfirmQueueName;

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setMessageListener(listenerAdapter);
        container.setQueueNames(emailConfirmQueueName);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(EmailConfirmationListener listener) {
        return new MessageListenerAdapter(listener);
    }
}
