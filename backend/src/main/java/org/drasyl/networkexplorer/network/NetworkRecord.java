package org.drasyl.networkexplorer.network;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Set;

public class NetworkRecord {
    @Id
    private String id;
    @CreatedDate
    private Date createdDate;
    private final Set<Node> nodes;

    public NetworkRecord(final Set<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "NetworkRecord{" +
                "id='" + id + '\'' +
                ", createdDate=" + createdDate +
                ", nodes=" + nodes +
                '}';
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
