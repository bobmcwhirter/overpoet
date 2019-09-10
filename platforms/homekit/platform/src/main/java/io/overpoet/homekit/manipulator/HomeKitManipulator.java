package io.overpoet.homekit.manipulator;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.server.model.impl.ServerServiceImpl;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.ApparatusType;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.measurement.Temperature;
import io.overpoet.spi.metadata.ApparatusMetadata;
import io.overpoet.spi.metadata.BooleanMetadata;
import io.overpoet.spi.sensor.Sensor;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.impl.ServerAccessoryImpl;
import io.overpoet.homekit.server.Bridge;

import static io.overpoet.hap.common.model.Permission.NOTIFY;
import static io.overpoet.spi.apparatus.ApparatusType.LIGHT;
import static io.overpoet.spi.apparatus.ApparatusType.THERMOMETER;
import static io.overpoet.hap.common.model.Permission.PAIRED_READ;
import static io.overpoet.hap.common.model.Permission.PAIRED_WRITE;

public class HomeKitManipulator implements Manipulator {

    public HomeKitManipulator(Bridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void connect(Apparatus apparatus) {
        ApparatusMetadata metadata = apparatus.metadata();
        ApparatusType type = metadata.type();

        System.err.println( type + " ==> " + apparatus.aspects());

        ServerAccessoryImpl accessory = new ServerAccessoryImpl(aid.incrementAndGet(), a -> {
            a.addService(iid.incrementAndGet(), Services.ACCESSORY_INFORMATION, (s) -> {
                configureAccessoryInformation(metadata, s);
            });
            if (type == THERMOMETER) {
                a.addService(iid.incrementAndGet(), Services.TEMPERATURE_SENSOR, s -> {
                    for (Aspect<?, ?> aspect : apparatus.aspects()) {
                        if (aspect.datatype() == Temperature.class) {
                            if (aspect.isSensor()) {
                                s.addCharacteristic(iid.incrementAndGet(), Characteristics.CURRENT_TEMPERATURE, c -> {
                                    c.setStoredValue(0.0);
                                    c.setPermissions(Permission.PAIRED_READ, NOTIFY);
                                    aspect.sensor(Temperature.class).onChange((t) -> {
                                        c.updateValue(t.celsius());
                                    });
                                });
                            }
                        }
                    }
                });
            } else if (type == LIGHT) {
                a.addService(iid.incrementAndGet(), Services.LIGHTBULB, s -> {
                    for (Aspect<?,?> aspect : apparatus.aspects()) {
                        if (aspect.datatype() == Boolean.class) {
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.ON, c -> {
                                c.setStoredValue(false);
                                c.setPermissions(PAIRED_READ, PAIRED_WRITE, NOTIFY);
                                //sensor.up(c::updateValue);
                                aspect.sensor(Boolean.class).onChange((t) -> {
                                    System.err.println("updating on/off to: " + t);
                                    c.updateValue(t);
                                });
                            });
                        } else if (aspect.datatype() == Integer.class) {
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.BRIGHTNESS, c -> {
                                c.setStoredValue(0);
                                c.setPermissions(PAIRED_READ, PAIRED_WRITE, NOTIFY);
                                //sensor.onChange(c::updateValue);
                                aspect.sensor(Integer.class).onChange((t) -> {
                                    System.err.println("updating brightness to: " + t);
                                    c.updateValue(t);
                                });
                            });
                        }
                    }
                });
            }
        });
        bridge.addAccessory(accessory);
    }

    private void configureAccessoryInformation(ApparatusMetadata metadata, ServerServiceImpl s) {
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.MANUFACTURER, (c) -> {
            c.setStoredValue(metadata.manufacturer());
            c.setPermissions(PAIRED_READ);
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.MODEL, (c) -> {
            c.setStoredValue(metadata.model());
            c.setPermissions(PAIRED_READ);
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, (c) -> {
            System.err.println("set stored: " + metadata.name());
            c.setStoredValue(metadata.name());
            c.setPermissions(PAIRED_READ);
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.SERIAL_NUMBER, (c) -> {
            c.setStoredValue(metadata.serialNumber());
            c.setPermissions(PAIRED_READ);
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.FIRMWARE_REVISION, (c) -> {
            c.setStoredValue(metadata.version());
            c.setPermissions(PAIRED_READ);
        });
        s.addCharacteristic(iid.incrementAndGet(), Characteristics.IDENTIFY, (c) -> {
            c.setStoredValue(null);
            c.setPermissions(PAIRED_WRITE);
        });
    }

    private final Bridge bridge;

    private final AtomicInteger aid = new AtomicInteger(1);

    private final AtomicInteger iid = new AtomicInteger(31);
}
