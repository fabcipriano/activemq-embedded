
package com.facio.messaging;

import static com.facio.config.JmsConfig.ORDER_QUEUE;
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

    @JmsListener(destination = ORDER_QUEUE)
    public void receiveMessage(@Payload Order order,
                               @Headers MessageHeaders headers,
                               Message message, Session session) {
        log.info("received <" + order + ">");

        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("######          Message Details           #####");
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("headers: " + headers);
        log.info("message: " + message);
        log.info("session: " + session);
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
    }

}
