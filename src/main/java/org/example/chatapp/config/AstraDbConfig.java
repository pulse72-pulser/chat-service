package org.example.chatapp.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import javax.annotation.PostConstruct;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class AstraDbConfig {

    private static final Logger log = LoggerFactory.getLogger(AstraDbConfig.class);

    @Value("${astra.db.secure-connect-bundle}")
    private String secureConnectBundle;

    @Value("${astra.db.keyspace}")
    private String keyspace;

    @Value("${astra.db.username}")
    private String username;

    @Value("${astra.db.password}")
    private String password;

    @PostConstruct
    public void validateProperties() {
        Assert.hasText(secureConnectBundle, "Secure connect bundle must not be empty");
        Assert.hasText(keyspace, "Keyspace must not be empty");
        Assert.hasText(username, "Username must not be empty");
        Assert.hasText(password, "Password must not be empty");
    }

    @Bean
    public CqlSession cqlSession() {
        try {
            Path bundle = Paths.get(secureConnectBundle);
            return CqlSession.builder()
                    .withCloudSecureConnectBundle(bundle)
                    .withAuthCredentials(username, password)
                    .withKeyspace(keyspace)
                    .build();
        } catch (Exception e) {
            log.error("Failed to create CqlSession: {}", e.getMessage(), e);
            throw e;
        }
    }

}
