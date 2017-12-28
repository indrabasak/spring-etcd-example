package com.basaki.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code EtcdProperties} represents the {@code etcd} database
 * properties. The properties are declared as etcd.uris.
 * <pre>
 * etcd:
 *   uris: http://host1:2379, http://host2:2379
 * </pre>.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/27/17
 */
@Data
@ConfigurationProperties("etcd")
public class EtcdProperties {

    private List<String> uris = new ArrayList<>();
}
