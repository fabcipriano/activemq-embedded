package com.facio.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

/**
 *
 * @author fabiano
 */
@Configuration
@EnableJms
public class JmsConfig {

    private static Logger LOG = LogManager.getLogger();
    public static final String ORDER_QUEUE = "order-queue";

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    @DependsOn(value = { "embedded-activemq" })
    public ActiveMQConnectionFactory connectionFactory() {
        LOG.info("Creating connectionFactory ...");
        if ("".equals(user)) {
            return new ActiveMQConnectionFactory(brokerUrl);
        }
        return new ActiveMQConnectionFactory(user, password, brokerUrl);
    }

    public CachingConnectionFactory cachingConnectionFactoryForProducer() {
        CachingConnectionFactory cache = new CachingConnectionFactory(connectionFactory());
        cache.setCacheConsumers(false);
        cache.setCacheProducers(true);
        cache.setSessionCacheSize(4);
        return cache;
    }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        LOG.info("Creating jmsTemplate ...");
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactoryForProducer());
        jmsTemplate.setMessageConverter(messageConverter());
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory queueListenerFactory( DefaultJmsListenerContainerFactoryConfigurer configurer ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory());
        factory.setMessageConverter(messageConverter());
        factory.setSessionTransacted(true);
        factory.setConcurrency("5-20");
        factory.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
        
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        LOG.info("Creating messageConverter ...");
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
