package com.example.spring.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.ConnectionFactory;
import java.util.Arrays;

@Configuration
@ComponentScan(basePackages = "com.example.spring")
@EnableJms//to enable support for the @JmsListener annotation that was used on the Receiver.(to enable detection of JmsListener annotation)
@PropertySource("application.properties")
public class ReceiverConfig {

    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Autowired
    Receiver receiver;   //for asynchronous receive without annotation


    @Bean
    public ConnectionFactory receiverActiveMQConnectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        //activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setTrustedPackages(Arrays.asList("com.example.spring"));

        return activeMQConnectionFactory;
    }


    //Message Listener Container used for invoking messageReceiver.onMessage on message reception (asynchronous receive without annotation)
   /* @Bean
    public MessageListenerContainer getContainer(){
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(receiverActiveMQConnectionFactory());
        container.setDestinationName("temp");
        container.setMessageListener(receiver);  //setting MessageListener as Receiver class(calls onMessage() method of Receiver)
        //container.setPubSubDomain(true);     //to receive message from Topic
        return container;
    }*/


    //it will receive message from the queue and pass it on the bean which is annotated with @JmsListener
    @Bean("factory1")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory1(){

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(receiverActiveMQConnectionFactory());
        factory.setConcurrency("1-1");
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean("factory2")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory2(){

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(receiverActiveMQConnectionFactory());
        factory.setConcurrency("1-1");
        factory.setPubSubDomain(true);
        return factory;
    }

   /* @Bean
    public JmsTemplate jmsTemplate(){           //required on receiver side only for synchronous receive(i.e for jmsTemplate.receive() )
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(receiverActiveMQConnectionFactory());
        template.setDefaultDestinationName("temp");
        //template.setPubSubDomain(true);       //to receive message from topic
        return template;
    }*/

    /*@Bean
    public Receiver receiver(){
        return new Receiver();
    }*/

    @Bean
    MessageConverter converter(){
        return new SimpleMessageConverter();
    }

}
