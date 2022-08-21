package org.drasyl.networkexplorer.network;

import com.google.common.cache.CacheBuilder;
import com.google.common.primitives.Bytes;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.drasyl.identity.DrasylAddress;
import org.drasyl.util.logging.Logger;
import org.drasyl.util.logging.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utility class for IP geolocation.
 */
public class IpLocations {
    public static final int MAX_OFFSET_METERS = 100;

    private static final Logger LOG = LoggerFactory.getLogger(IpLocations.class);
    private static final DatabaseReader DB_READER;
    private static final MessageDigest SHA2;

    static {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("GeoLite2-City.mmdb");
        DatabaseReader dbReader;
        try {
            dbReader = new DatabaseReader.Builder(stream).build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        DB_READER = dbReader;

        MessageDigest sha2;
        try {
            sha2 = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SHA2 = sha2;
    }

    private static final Map<InetAddress, Location> cache = CacheBuilder.newBuilder()
            .maximumSize(1_000)
            .<InetAddress, Location>build()
            .asMap();

    public static Location locate(final Map<InetAddress, Location> staticLocations,
                                  final InetAddress address) {
        final Location staticLocation = staticLocations.get(address);
        if (staticLocation != null) {
            return staticLocation;
        }

        return cache.computeIfAbsent(address, k -> {
            final CityResponse city;
            try {
                city = DB_READER.city(address);
                final Location location = new Location(city.getLocation().getLatitude(), city.getLocation().getLongitude());
                LOG.info("Located IP address `{}` to location `{}`.", k, location);
                return location;
            }
            catch (IOException | GeoIp2Exception e) {
                LOG.error("Unable to locate ip address `{}`:", k, e);
                return null;
            }
        });
    }

    public static Location locateWithNoise(final Map<InetAddress, Location> staticLocations,
                                           final String salt,
                                           final DrasylAddress address,
                                           final InetAddress inetAddress) {
        byte[] hash = SHA2.digest(Bytes.concat(salt.getBytes(UTF_8), address.toByteArray()));
        ByteBuffer buffer = ByteBuffer.allocate(32).put(hash);

        double rand1 = (buffer.getInt(0) & 0xffffffffL) / 4_294_967_296.0;
        double rand2 = (buffer.getInt(4) & 0xffffffffL) / 4_294_967_296.0;
        double rand3 = (buffer.getInt(8) & 0xffffffffL) / 4_294_967_296.0;

        Location location = locate(staticLocations, inetAddress);

        if (location == null) {
            return null;
        }

        // random point within circle around location
        final double t = rand1 * 2 * Math.PI; // direction
        final double u = rand2 + rand3; // distance to point
        final double r = u > 1 ? 2 - u : u;
        double yLatOffset = r * Math.sin(t);
        double xLongOffset = r * Math.cos(t);

        //Position, decimal degrees
        final double lat = location.getLatitude();
        final double lon = location.getLongitude();

        //Earth’s radius, sphere
        final double R = 6378137;

        //offsets in meters
        final double dn = yLatOffset * MAX_OFFSET_METERS;
        final double de = xLongOffset * MAX_OFFSET_METERS;

        //Coordinate offsets in radians
        final double dLat = dn / R;
        final double dLon = de / (R * Math.cos(Math.PI * lat / 180));

        //OffsetPosition, decimal degrees
        final double latO = lat + dLat * 180 / Math.PI;
        final double lonO = lon + dLon * 180 / Math.PI;

        return new Location(latO, lonO);
    }
}
