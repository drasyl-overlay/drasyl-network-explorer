package org.drasyl.networkexplorer.network;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "network")
public class NetworkConfiguration {
    private String geoipSalt;
    private List<StaticLocation> staticLocations;
    private Map<InetAddress, Location> staticLocationsMap;

    public String getGeoipSalt() {
        return geoipSalt;
    }

    public void setGeoipSalt(final String geoipSalt) {
        this.geoipSalt = geoipSalt;
    }

    public List<StaticLocation> getStaticLocations() {
        return staticLocations;
    }

    public void setStaticLocations(final List<StaticLocation> staticLocations) {
        this.staticLocations = staticLocations;
    }

    public synchronized Map<InetAddress, Location> getStaticLocationsMap() {
        if (staticLocationsMap == null) {
            staticLocationsMap = new HashMap<>();
            for (StaticLocation staticLocation : getStaticLocations()) {
                staticLocationsMap.put(staticLocation.getAddress(), staticLocation.getLocation());
            }
        }
        return staticLocationsMap;
    }
}
