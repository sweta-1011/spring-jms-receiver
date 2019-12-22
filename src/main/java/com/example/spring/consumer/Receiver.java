package com.example.spring.consumer;
import com.example.spring.producer.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;


//implements MessageListener (only for asynchronous receive without annotation)
@Component
public class Receiver  {

    @Autowired
    MessageConverter messageConverter;

    /*@Autowired
    JmsTemplate jmsTemplate;   //required on receiver side only for synchronous receive(i.e for jmsTemplate.receive)
    */

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);


   /*------------------------- Synchronous receive ----------------------------------
    public Product receiveMessage(){

        try{
            Message message = jmsTemplate.receive("temp");
            Product response = (Product) messageConverter.fromMessage(message);     //receiving Product message
            return response;
        }
        catch (Exception exe){
            exe.printStackTrace();
        }
        return null;
    }*/

   /*public String receiveMessage(){

        try{
            Message message = jmsTemplate.receive("temp");
                String response = (String) messageConverter.fromMessage(message);     //receiving String message
            return response;
        }
        catch (Exception exe){
            exe.printStackTrace();
        }
        return null;
    }*/


   //----------------------for Asynchronous message receive (without annotation) ---------------

    /*public void onMessage(Message message) {

        try {
            Product response = (Product) messageConverter.fromMessage(message);

            System.out.println("...Inside onMessage()...");
            System.out.println(response);
            System.out.println("...Inside onMessage()...");
        }
        catch (JMSException e){
            e.printStackTrace();
        }

    }
*/

    //----------------------for Asynchronous message receive (using annotation)--------------------------------

        @JmsListener(destination = "temp1", containerFactory = "factory1")
        public void receiveMessage1(Product message)throws JMSException{
            //listener container behind the scenes for each annotated method, using a JmsListenerContainerFactory

//            MessageHeaders headers = message.getHeaders();
//            System.out.println(headers);
            System.out.println("received message ="+ message + " from temp1");
        }

        @JmsListener(destination = "temp2", containerFactory = "factory2")
        public void receiveMessag2(Product message)throws JMSException{
            //listener container behind the scenes for each annotated method, using a JmsListenerContainerFactory

            System.out.println("received message ="+ message + " from temp2");
        }

}




//----Queue(point-to-point)---
    //-- there is only 1 client for each message.
//-1: Asynchronous receive - the sender sends many messages to queue and when the receiver appln starts, it receives all the messages.
//-2: Synchronous receive - the sender sends many messages to queue and when receiver appln starts, it receives 1 message, then it
//                           starts again and receives 2nd message etc.

//-----Topic(pub-sub)---
//-1: Asynchronous receive - the receiver subscribes for topic(and is active), then sender sends many messages. It will receive all the
//                           messages which were sent after tat subscription, provided the receive is active.
//-2: Synchronous receive - the receiver subscribes for topic(and is active), then sends many messages. it will receive only
//                           1 message which was sent after tat subscription(and not all), provided the receiver is active.