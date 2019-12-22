package com.example.spring;

import com.example.spring.consumer.ReceiverConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MessageReceiverApp {

    public static void main(String[] args){

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(ReceiverConfig.class);

        //---for asynchronous message receiving---
        try {
            Thread.sleep(6000000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

//        Receiver receiver = context.getBean(Receiver.class);
//        Product response = receiver.receiveMessage();
//
//        String response = receiver.receiveMessage();
//        System.out.println("message received successfully : " + response);

        context.close();

    }

}