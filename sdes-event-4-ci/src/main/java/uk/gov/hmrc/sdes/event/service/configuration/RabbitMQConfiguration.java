package uk.gov.hmrc.sdes.event.service.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("sdes.exchange", true, false);
    }

    @Bean
    public Binding fileEventBinding() {
        return BindingBuilder.bind(fileEventQueue())
                .to(exchange())
                .with("messages.key");
    }

    @Bean
    public Binding notficationEventBinding() {
        return BindingBuilder.bind(notificationEventQueue())
                .to(exchange())
                .with("sdes.notification.key");
    }

    @Bean
    public Queue fileEventQueue() {
        return new Queue("sdes.file.event.queue", true);
    }

    @Bean
    public Queue notificationEventQueue() {
        return new Queue("sdes.notification.event.queue", true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

}
