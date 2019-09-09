package io.overpoet.homekit.manipulator;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.server.model.impl.ServerServiceImpl;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.ApparatusType;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.metadata.ApparatusMetadata;
import io.overpoet.spi.metadata.BooleanMetadata;
import io.overpoet.spi.sensor.BooleanSensor;
import io.overpoet.spi.sensor.PercentageSensor;
import io.overpoet.spi.sensor.Sensor;
import io.overpoet.spi.sensor.TemperatureSensor;
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
        //if ( type != THERMOMETER ) {
        //return;
        //}
        ServerAccessoryImpl accessory = new ServerAccessoryImpl(aid.incrementAndGet(), a -> {
            a.addService(iid.incrementAndGet(), Services.ACCESSORY_INFORMATION, (s) -> {
                configureAccessoryInformation(metadata, s);
            });
            if (type == THERMOMETER) {
                a.addService(iid.incrementAndGet(), Services.TEMPERATURE_SENSOR, s -> {
                    for (Sensor<?> sensor : apparatus.sensors()) {
                        if (sensor instanceof TemperatureSensor) {
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.CURRENT_TEMPERATURE, c -> {
                                //c.setStoredValue();
                                c.setStoredValue(0.0);
                                c.setPermissions(Permission.PAIRED_READ, NOTIFY);
                                ((TemperatureSensor) sensor).onChange((t) -> {
                                    c.updateValue(t.celsius());
                                });
                            });
                            //s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, c-> {
                            //c.setStoredValue(sensor.key());
                            //c.setPermissions(Permission.PAIRED_READ);
                            //});
                        }
                    }
                });
            } else if (type == LIGHT) {
                a.addService(iid.incrementAndGet(), Services.LIGHTBULB, s -> {
                    for (Sensor<?> sensor : apparatus.sensors()) {
                        if (sensor instanceof BooleanSensor) {
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.ON, c -> {
                                c.setStoredValue(false);
                                c.setPermissions(PAIRED_READ, PAIRED_WRITE, NOTIFY);
                                //sensor.up(c::updateValue);
                                sensor.onChange((t) -> {
                                    System.err.println("updating on/off to: " + t);
                                    c.updateValue(t);
                                });
                            });
                        } else if (sensor instanceof PercentageSensor) {
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.BRIGHTNESS, c -> {
                                c.setStoredValue(0);
                                c.setPermissions(PAIRED_READ, PAIRED_WRITE, NOTIFY);
                                //sensor.onChange(c::updateValue);
                                sensor.onChange((t) -> {
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
