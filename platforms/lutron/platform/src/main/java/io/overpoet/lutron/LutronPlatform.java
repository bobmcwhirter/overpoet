package io.overpoet.lutron;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import io.overpoet.lutron.leap.client.Client;
import io.overpoet.lutron.leap.client.model.Device;
import io.overpoet.lutron.leap.client.model.SwitchedLevel;
import io.overpoet.lutron.leap.client.model.Universe;
import io.overpoet.lutron.leap.client.model.Zone;
import io.overpoet.lutron.leap.client.model.ZoneStatus;
import io.overpoet.spi.Key;
import io.overpoet.spi.actuator.Actuator;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.ApparatusType;
import io.overpoet.spi.apparatus.SimpleApparatus;
import io.overpoet.spi.metadata.ApparatusMetadata;
import io.overpoet.spi.metadata.IntegerMetadata;
import io.overpoet.spi.metadata.SimpleApparatusMetadata;
import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformContext;
import io.overpoet.spi.sensor.PercentageSensor;
import io.overpoet.spi.sensor.Sensor;
import io.overpoet.spi.sensor.SensorLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LutronPlatform implements Platform, ServiceListener {
    private static final Logger LOG = LoggerFactory.getLogger("io.overpoet.lutron");

    @Override
    public void initialize(PlatformContext context) {
        this.key = Key.of("lutron");
        this.universe = new Universe();

        this.universe.onZoneAdded(this::onZoneAdded);
        this.universe.onZoneStatusUpdated(this::onZoneStatusUpdated);

        this.context = context;
        try {
            this.mdns = JmDNS.create();
            this.mdns.addServiceListener("_leap._tcp.local.", this);
        } catch (IOException e) {
            LOG.error("Error while discoverying bridge", e);
        }
    }

    protected void onZoneAdded(Zone zone) {
        LOG.info("Zone added: {}", zone.name());
        context.connect(createApparatus(zone));
    }

    private Apparatus createApparatus(Zone zone) {
        ApparatusMetadata metadata = createMetadata(zone);
        Set<Sensor<?>> sensors = createSensors(zone);
        Set<Actuator<?>> actuators = createActuators(zone);
        SimpleApparatus apparatus = new SimpleApparatus(metadata, key.append(zone.href()), sensors, actuators);
        return apparatus;
    }

    private ApparatusMetadata createMetadata(Zone zone) {
        Device device = zone.device();
        SimpleApparatusMetadata metadata = new SimpleApparatusMetadata(ApparatusType.LIGHT,
                                                                       zone.name(),
                                                                       "Lutron",
                                                                       device.modelNumber(),
                                                                       "1.0",
                                                                       device.serialNumber()
        );
        return metadata;
    }

    Set<Sensor<?>> createSensors(Zone zone) {
        DelegateSensorLogic<Integer> logic = new DelegateSensorLogic<Integer>();
        PercentageSensor sensor = new PercentageSensor(this.key.append(zone.href()), IntegerMetadata.DEFAULT, logic);
        this.logics.put(zone, logic);
        return Collections.singleton(sensor);
    }

    private Map<Zone, DelegateSensorLogic<Integer>> logics = new HashMap<>();

    Set<Actuator<?>> createActuators(Zone zone) {
        return Collections.emptySet();
    }


    protected void onZoneStatusUpdated(ZoneStatus status) {
        LOG.info("Zone status updated: {}", status.zone().name());
        DelegateSensorLogic<Integer> logic = this.logics.get(status.zone());
        if ( logic == null ) {
            LOG.warn("No logic for {}", status.zone().name());
            return;
        }
        if ( status.switchedLevel() == SwitchedLevel.OFF ) {
            LOG.debug("delegate {} to OFF", status.zone().name() );
            logic.delegate( 0 );
        } else {
            LOG.debug("delegate {} to {}", status.zone().name(), status.level() );
            logic.delegate(status.level());
        }
    }

    private Map<Zone, Apparatus> apparatuses = new HashMap<>();

    @Override
    public void serviceAdded(ServiceEvent event) {
        //System.err.println("added: " + event);
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        //System.err.println("removed: " + event);
    }

    @Override
    public synchronized void serviceResolved(ServiceEvent event) {
        Inet4Address[] addresses = event.getInfo().getInet4Addresses();
        if (addresses.length == 0) {
            this.bridgeLocation = null;
            return;
        }

        if (this.bridgeLocation != null) {
            return;
        }

        int port = event.getInfo().getPort();

        this.bridgeLocation = new InetSocketAddress(addresses[0], port);

        this.client = new Client(this.universe);
        try {
            LOG.info("Connecting: {}", this.bridgeLocation);
            this.client.connect(this.bridgeLocation);
            LOG.info("Connected");
        } catch (Exception e) {
            LOG.error("Unable to connect to bridge", e);
        }
    }

    private PlatformContext context;

    private JmDNS mdns;

    private InetSocketAddress bridgeLocation;

    private Client client;

    private Universe universe;

    private Key key;
}
