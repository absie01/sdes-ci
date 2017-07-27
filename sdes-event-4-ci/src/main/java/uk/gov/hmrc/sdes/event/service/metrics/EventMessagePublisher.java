package uk.gov.hmrc.sdes.event.service.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import uk.gov.hmrc.sdes.event.service.dto.Event;

@Service
public class EventMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(EventMessagePublisher.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EventMessagePublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 5)
    public void sendMessage(final String exchange, final String routingKey, final Event message) {
        log.debug("Sending message..." + message.toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
