package org.drasyl.networkexplorer.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.drasyl.identity.DrasylAddress;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class Node {
    private DrasylAddress address;
    private Location location;
    private Set<DrasylAddress> superPeers;

    private Node() {
    }

    public Node(final DrasylAddress address,
                final Location location,
                final Set<DrasylAddress> superPeers) {
        this.address = requireNonNull(address);
        this.location = requireNonNull(location);
        this.superPeers = requireNonNull(superPeers);
    }

    public Node(final DrasylAddress address, final Location location) {
        this(address, location, new HashSet<>());
    }

    public DrasylAddress getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Set<DrasylAddress> getSuperPeers() {
        return Set.copyOf(superPeers);
    }

    public boolean addSuperPeer(final DrasylAddress superPeer) {
        return superPeers.add(superPeer);
    }

    @JsonIgnore
    public boolean isClient() {
        return !superPeers.isEmpty();
    }

    @JsonIgnore
    public boolean isSuperPeer() {
        return !isClient();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Node node = (Node) o;
        return Objects.equals(address, node.address) && Objects.equals(location, node.location) && Objects.equals(superPeers, node.superPeers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, location, superPeers);
    }
}
