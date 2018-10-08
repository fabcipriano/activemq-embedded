package com.facio;

import com.facio.messaging.Order;
import com.facio.messaging.OrderSender;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AMQApplication implements ApplicationRunner {

    private static Logger LOG = LogManager.getLogger();
    private final AtomicLong count = new AtomicLong(0L);

    @Autowired
    private OrderSender orderSender;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOG.info("Start sending messages ...");
        LOG.info("Spring Boot Embedded ActiveMQ Configuration Example");

        sendMessages(5);
        LOG.info("Waiting for all ActiveMQ JMS Messages to be consumed");
        TimeUnit.SECONDS.sleep(300);
        
    }

    public void sendMessages(int total) throws InterruptedException {
        for (int i = 1; i <= total; i++) {            
            Order myMessage = new Order(count.incrementAndGet() + " - Sending JMS Message using Embedded activeMQ", new Date());
            orderSender.send(myMessage);
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(AMQApplication.class, args);
    }
}
