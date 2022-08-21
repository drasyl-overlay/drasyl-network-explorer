package org.drasyl.networkexplorer.serialization;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.drasyl.crypto.HexUtil;

import java.io.IOException;

/**
 * Provides hints for correct {@link org.drasyl.identity.Key} JSON (de)serialization.
 */
@SuppressWarnings("java:S118")
public interface KeyMixin {
    @JsonValue
    @JsonSerialize(using = BytesToHexStringSerializer.class)
    @JsonDeserialize(using = BytesToHexStringDeserializer.class)
    byte[] toByteArray();

    /**
     * Deserializes a given hexadecimal string to byte array.
     */
    @SuppressWarnings("java:S4926")
    final class BytesToHexStringDeserializer extends StdDeserializer<byte[]> {
        private static final long serialVersionUID = 3616936627408179992L;

        public BytesToHexStringDeserializer() {
            super(byte[].class);
        }

        public BytesToHexStringDeserializer(final Class<byte[]> t) {
            super(t);
        }

        @Override
        public byte[] deserialize(final JsonParser p,
                                  final DeserializationContext ctxt) throws IOException {
            return HexUtil.fromString(p.getText());
        }
    }

    /**
     * Serializes a given byte array as hexadecimal string.
     */
    @SuppressWarnings("java:S4926")
    final class BytesToHexStringSerializer extends StdSerializer<byte[]> {
        private static final long serialVersionUID = 4261135293288643562L;

        public BytesToHexStringSerializer() {
            super(byte[].class);
        }

        public BytesToHexStringSerializer(final Class<byte[]> t) {
            super(t);
        }

        @Override
        public void serialize(final byte[] value,
                              final JsonGenerator gen,
                              final SerializerProvider provider) throws IOException {
            gen.writeString(HexUtil.bytesToHex(value));
        }
    }
}

