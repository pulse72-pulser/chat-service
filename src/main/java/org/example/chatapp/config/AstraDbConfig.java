package org.example.chatapp.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class AstraDbConfig {

    @Value("${astra.db.secure-connect-bundle}")
    private String secureConnectBundle;

    @Value("${astra.db.keyspace}")
    private String keyspace;

    @Value("${astra.db.username}")
    private String username;

    @Value("${astra.db.password}")
    private String password;

//    @Value("${astra.db.token}")
//    private String token;

    @Bean
    public CqlSession cqlSession() {
        Path bundle = Paths.get(secureConnectBundle);
        return CqlSession.builder()
                .withCloudSecureConnectBundle(bundle)
                .withAuthCredentials(username, password)
                .withKeyspace(keyspace)
                .build();
    }
}