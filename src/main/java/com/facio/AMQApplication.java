package com.facio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AMQApplication implements ApplicationRunner {
    Logger LOG = LogManager.getLogger();

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOG.info("Start sending messages ...");
    }

    public static void main(String[] args) {
        SpringApplication.run(AMQApplication.class, args);
    }
}
