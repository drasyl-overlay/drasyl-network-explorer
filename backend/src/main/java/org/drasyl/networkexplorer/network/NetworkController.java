package org.drasyl.networkexplorer.network;

import com.google.common.cache.CacheBuilder;
import org.drasyl.identity.DrasylAddress;
import org.drasyl.networkexplorer.telemetry.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import static java.time.Duration.ofMinutes;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController

public class NetworkController {
    private final Logger logger = LoggerFactory.getLogger(NetworkController.class);
    private final Map<InetAddress, Topology> topologies;
    @Autowired
    private NetworkRecordRepository recordRepository;
    @Autowired
    private NetworkConfiguration configuration;

    public NetworkController() {
        topologies = CacheBuilder
                .newBuilder()
                .expireAfterWrite(ofMinutes(2))
                .maximumSize(1_000)
                .<InetAddress, Topology>build()
                .asMap();
    }

    /**
     * Returns all currently known nodes in our network.
     */
    @GetMapping(value = "/network-1.json", produces = APPLICATION_JSON_VALUE)
    public Set<Node> getNodes() {
        Map<DrasylAddress, Node> nodes = new HashMap<>();
        for (Entry<InetAddress, Topology> entry : topologies.entrySet()) {
            final InetAddress reportingInetAddress = entry.getKey();
            final Topology topology = entry.getValue();

            // add reporting note
            try {
                final Node reportingNode = new Node(topology.getAddress(), IpLocations.locateWithNoise(configuration.getStaticLocationsMap(), configuration.getGeoipSalt(), topology.getAddress(), reportingInetAddress));
                nodes.put(topology.getAddress(), reportingNode);

                // add super peers
                for (Entry<DrasylAddress, InetSocketAddress> entry2 : topology.getSuperPeers().entrySet()) {
                    final DrasylAddress address = entry2.getKey();
                    final InetAddress inetAddress = entry2.getValue().getAddress();
                    final Location location;
                    if (!inetAddress.isSiteLocalAddress()) {
                        location = IpLocations.locateWithNoise(configuration.getStaticLocationsMap(), configuration.getGeoipSalt(), address, inetAddress);
                    }
                    else {
                        // fallback address
                        location = reportingNode.getLocation();
                    }

                    if (location != null) {
                        nodes.computeIfAbsent(address, k -> new Node(address, location));
                        reportingNode.addSuperPeer(address);
                    }
                }

                // add children peers
                for (Entry<DrasylAddress, InetSocketAddress> entry2 : topology.getChildrenPeers().entrySet()) {
                    final DrasylAddress address = entry2.getKey();
                    final InetAddress inetAddress = entry2.getValue().getAddress();
                    final Location location;
                    if (!inetAddress.isSiteLocalAddress()) {
                        location = IpLocations.locateWithNoise(configuration.getStaticLocationsMap(), configuration.getGeoipSalt(), address, inetAddress);
                    }
                    else {
                        // fallback address
                        location = reportingNode.getLocation();
                    }

                    if (location != null) {
                        final Node childrenPeer = nodes.computeIfAbsent(address, k -> new Node(address, location));
                        childrenPeer.addSuperPeer(reportingNode.getAddress());
                    }
                }
            }
            catch (final NullPointerException e) {
                logger.warn("Ignore topology as reporting InetAddress could not be located.", e);
            }
        }
        final TreeSet<Node> set = new TreeSet<>(Comparator.comparing(o -> o.getAddress().toString()));
        set.addAll(nodes.values());
        return set;
    }

    public void addTopology(final InetAddress address, final Topology topology) {
        topologies.put(address, topology);
    }

    @Scheduled(fixedRate = 60_000, initialDelay = 60_000)
    void trackNetwork() {
        final NetworkRecord lastRecord = recordRepository.findFirstByOrderByCreatedDateDesc();
        final Set<Node> nodes = getNodes();
        if (lastRecord == null || !lastRecord.getNodes().equals(nodes)) {
            recordRepository.save(new NetworkRecord(nodes));
        }
    }
}
