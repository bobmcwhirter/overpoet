package io.overpoet.homekit.manipulator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.ServerService;
import io.overpoet.hap.server.model.impl.ServerAccessoryImpl;
import io.overpoet.hap.server.model.impl.ServerServiceImpl;
import io.overpoet.homekit.manipulator.handlers.LightHandler;
import io.overpoet.homekit.manipulator.handlers.ThermometerHandler;
import io.overpoet.homekit.server.Bridge;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.ApparatusType;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.measurement.Temperature;
import io.overpoet.spi.metadata.ApparatusMetadata;

import static io.overpoet.spi.apparatus.ApparatusType.LIGHT;
import static io.overpoet.spi.apparatus.ApparatusType.THERMOMETER;

public class HomeKitManipulator implements Manipulator {

    private static Map<ApparatusType, ApparatusHandler> HANDLERS = new HashMap<>() {{
        put(THERMOMETER, new ThermometerHandler());
        put(LIGHT, new LightHandler());
    }};

    public HomeKitManipulator(Bridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void connect(Apparatus apparatus) {
        ApparatusMetadata metadata = apparatus.metadata();
        ApparatusType type = metadata.type();

        ServerAccessoryImpl accessory = new ServerAccessoryImpl(aid.incrementAndGet(), a -> {
            a.addService(iid.incrementAndGet(), Services.ACCESSORY_INFORMATION, (s) -> {
                configureAccessoryInformation(metadata, s);
            });
            ApparatusHandler handler = HANDLERS.get(type);
            if ( handler != null ) {
                handler.handle(iid, a, apparatus);
            }
        });
        bridge.addAccessory(accessory);
    }

    private void configureAccessoryInformation(ApparatusMetadata metadata, ServerService s) {
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.MANUFACTURER, (c) -> {
            c.setStoredValue(metadata.manufacturer());
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.MODEL, (c) -> {
            c.setStoredValue(metadata.model());
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, (c) -> {
            c.setStoredValue(metadata.name());
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.SERIAL_NUMBER, (c) -> {
            c.setStoredValue(metadata.serialNumber());
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.FIRMWARE_REVISION, (c) -> {
            c.setStoredValue(metadata.version());
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.IDENTIFY, (c) -> {
        });
    }

    private final Bridge bridge;

    private final AtomicInteger aid = new AtomicInteger(1);

    private final AtomicInteger iid = new AtomicInteger(31);
}
