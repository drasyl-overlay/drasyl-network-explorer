package org.drasyl.networkexplorer.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.drasyl.identity.IdentityPublicKey;

/**
 * Provides hints for correct {@link IdentityPublicKey} JSON (de)serialization.
 */
@SuppressWarnings("java:S118")
public interface IdentityPublicKeyMixin extends KeyMixin {
    @JsonCreator
    static IdentityPublicKey of(final String bytes) {
        return IdentityPublicKey.of(bytes);
    }
}
