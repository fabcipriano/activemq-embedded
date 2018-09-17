package com.facio.config;

import javax.sql.DataSource;
import org.apache.activemq.broker.BrokerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.apache.activemq.Service;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author fabiano
 */
@Configuration
public class EmbeddedAMQConfig {

    private static Logger LOG = LogManager.getLogger();

    @Bean("embedded-activemq")
    @DependsOn(value = { "mysqldatasource" })
    public Service createEmbeddedActiveMQ() {
        try {
            LOG.info("Creating embedded broker ...");
            BrokerService broker = new BrokerService();
            broker.setBrokerName("embedded-amq");
            broker.setUseJmx(true);
            broker.setUseShutdownHook(true);
            broker.setManagementContext(new ManagementContext());
            broker.setPersistent(true);
            broker.setPersistenceAdapter(createPersistenceMessaging());
            
            broker.addConnector("tcp://localhost:61616");
                        
            broker.start();
            return broker;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create ActiveMQ broker",ex);
        } finally {
            LOG.info("Created embedded broker.");
        }
    }
    
    public PersistenceAdapter createPersistenceMessaging() {
        JDBCPersistenceAdapter persistence = new JDBCPersistenceAdapter();
        persistence.setDataSource(dataSource());
        return persistence;
    }

    @Primary
    @Bean("mysqldatasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        LOG.info("Creating DATASOURCE ..............");
        return DataSourceBuilder
                .create()
                .build();
    }
}
