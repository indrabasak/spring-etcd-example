package com.basaki.config;

import com.coreos.jetcd.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code DataConfiguration} configures {@code etcd} database connection.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/27/17
 */
@Configuration
public class DataConfiguration {

    @Autowired
    private EtcdProperties properties;

    @Bean
    public Client etcdClient() {
        return Client.builder().endpoints(properties.getUris()).build();
    }
}
