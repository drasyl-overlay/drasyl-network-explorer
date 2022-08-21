package org.drasyl.networkexplorer;

import org.drasyl.identity.IdentityPublicKey;
import org.drasyl.networkexplorer.serialization.IdentityPublicKeyMixin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoAuditing
@EnableConfigurationProperties
public class NetworkExplorerApplication {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.mixIn(IdentityPublicKey.class, IdentityPublicKeyMixin.class);
        return builder;
    }

    public static void main(String[] args) {
        SpringApplication.run(NetworkExplorerApplication.class, args);
    }
}
