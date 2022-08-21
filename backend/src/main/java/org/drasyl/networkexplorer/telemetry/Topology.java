package org.drasyl.networkexplorer.telemetry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableMap;
import org.drasyl.identity.DrasylAddress;
import org.drasyl.identity.IdentityPublicKey;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Data object representing the topology view of a super peer.
 */
public class Topology {
    private final DrasylAddress address;
    private final Map<DrasylAddress, InetSocketAddress> superPeers;
    private final Map<DrasylAddress, InetSocketAddress> childrenPeers;
    private final Map<DrasylAddress, InetSocketAddress> peers;

    @JsonCreator
    public Topology(@JsonProperty("address") final DrasylAddress address,
                    @JsonProperty("superPeers") final Map<DrasylAddress, InetSocketAddress> superPeers,
                    @JsonProperty("childrenPeers") final Map<DrasylAddress, InetSocketAddress> childrenPeers,
                    @JsonProperty("peers") final Map<DrasylAddress, InetSocketAddress> peers) {
        this.address = requireNonNull(address, "address must not be null");
        this.superPeers = requireNonNull(superPeers, "superPeers must not be null");
        this.childrenPeers = requireNonNull(childrenPeers, "childrenPeers must not be null");
        this.peers = requireNonNull(peers, "peers must not be null");
    }

    @JsonDeserialize(as = IdentityPublicKey.class)
    public DrasylAddress getAddress() {
        return address;
    }

    @JsonDeserialize(keyAs = IdentityPublicKey.class)
    public Map<DrasylAddress, InetSocketAddress> getSuperPeers() {
        return ImmutableMap.copyOf(superPeers);
    }

    @JsonDeserialize(keyAs = IdentityPublicKey.class)
    public Map<DrasylAddress, InetSocketAddress> getChildrenPeers() {
        return ImmutableMap.copyOf(childrenPeers);
    }

    @JsonDeserialize(keyAs = IdentityPublicKey.class)
    public Map<DrasylAddress, InetSocketAddress> getPeers() {
        return ImmutableMap.copyOf(peers);
    }

    @Override
    public String toString() {
        return "Topology{" +
                "address=" + address +
                ", superPeers=" + superPeers +
                ", childrenPeers=" + childrenPeers +
                ", peers=" + peers +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Topology topology = (Topology) o;
        return Objects.equals(address, topology.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
