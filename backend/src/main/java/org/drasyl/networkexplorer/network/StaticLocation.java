package org.drasyl.networkexplorer.network;

import java.net.InetAddress;

public class StaticLocation {
    private InetAddress address;
    private double latitude;
    private double longitude;

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(final InetAddress address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }
}
