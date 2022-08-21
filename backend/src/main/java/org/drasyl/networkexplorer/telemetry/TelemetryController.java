package org.drasyl.networkexplorer.telemetry;

import org.drasyl.networkexplorer.network.NetworkController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Collects telemetry data sent by our super peers.
 */
@RestController
public class TelemetryController {
    private final Logger logger = LoggerFactory.getLogger(TelemetryController.class);
    @Autowired
    private NetworkController networkController;

    /**
     * Collects the topologies sent by our super peers.
     */
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void submit(@RequestBody Topology topology, HttpServletRequest request) {
        InetAddress remoteAddress = readRemoteAddress(request);
        if (remoteAddress != null) {
            networkController.addTopology(remoteAddress, topology);
        }
    }

    private InetAddress readRemoteAddress(final HttpServletRequest request) {
        try {
            String proxyAddr = request.getHeader("X-Forwarded-For");
            if (proxyAddr != null) {
                return InetAddress.getByName(proxyAddr);
            }
            else {
                return InetAddress.getByName(request.getRemoteAddr());
            }
        }
        catch (UnknownHostException e) {
            logger.info("Unable to read remote address from request.", e);
            return null;
        }
    }
}
