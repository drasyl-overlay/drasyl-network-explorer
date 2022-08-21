package org.drasyl.networkexplorer.network;

import org.drasyl.identity.DrasylAddress;
import org.drasyl.identity.IdentityPublicKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class Converters {
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new DrasylAddressToStringConverter(),
                new StringToDrasylAddressConverter()
        ));
    }

    @WritingConverter
    private static class DrasylAddressToStringConverter implements Converter<DrasylAddress, String> {
        @Override
        public String convert(final DrasylAddress source) {
            return source.toString();
        }
    }

    @ReadingConverter
    private static class StringToDrasylAddressConverter implements Converter<String, DrasylAddress> {
        @Override
        public DrasylAddress convert(final String source) {
            return IdentityPublicKey.of(source);
        }
    }
}
