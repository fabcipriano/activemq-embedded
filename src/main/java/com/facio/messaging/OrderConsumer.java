
package com.facio.messaging;

import static com.facio.config.JmsConfig.ORDER_QUEUE;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabiano
 */
@Component
public class OrderConsumer {
    private static Logger log = LogManager.getLogger();

    @JmsListener(destination = ORDER_QUEUE, containerFactory = "queueListenerFactory")
    public void receiveMessage(@Payload Order order,
                               @Headers MessageHeaders headers,
                               Message message, Session session) {
        waitFor();        
        log.info("after wait, JMS Received <" + order + ">; message.:" + message);
    }

    private void waitFor() throws RuntimeException {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            throw new RuntimeException("unexpected exception", ex);
        }
    }
}
