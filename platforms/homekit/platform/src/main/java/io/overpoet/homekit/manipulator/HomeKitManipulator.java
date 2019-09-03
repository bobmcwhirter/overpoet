package io.overpoet.homekit.manipulator;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.ApparatusType;
import io.overpoet.spi.manipulator.Manipulator;
import io.overpoet.spi.sensor.Sensor;
import io.overpoet.spi.sensor.TemperatureSensor;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Permission;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.impl.ServerAccessoryImpl;
import io.overpoet.homekit.server.Bridge;

import static io.overpoet.spi.apparatus.ApparatusType.THERMOMETER;
import static io.overpoet.hap.common.model.Permission.PAIRED_READ;
import static io.overpoet.hap.common.model.Permission.PAIRED_WRITE;

public class HomeKitManipulator implements Manipulator {

    public HomeKitManipulator(Bridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void connect(Apparatus apparatus) {
        ApparatusType type = apparatus.type();
        if ( type == THERMOMETER ) {
            ServerAccessoryImpl accessory = new ServerAccessoryImpl(aid.incrementAndGet(), a->{
                a.addService(iid.incrementAndGet(), Services.ACCESSORY_INFORMATION, (s) -> {
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.MANUFACTURER, (c)->{
                        c.setStoredValue("temp");
                        c.setPermissions(PAIRED_READ);
                    });
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.MODEL, (c)->{
                        c.setStoredValue("opb");
                        c.setPermissions(PAIRED_READ);
                    });
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, (c)->{
                        c.setStoredValue("current temp");
                        c.setPermissions(PAIRED_READ);
                    });
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.SERIAL_NUMBER, (c)->{
                        c.setStoredValue("8675309");
                        c.setPermissions(PAIRED_READ);
                    });
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.FIRMWARE_REVISION, (c)->{
                        c.setStoredValue("1.0.0");
                        c.setPermissions(PAIRED_READ);
                    });
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.IDENTIFY, (c)->{
                        c.setStoredValue(null);
                        c.setPermissions(PAIRED_WRITE);
                    });
                });
                a.addService(iid.incrementAndGet(), Services.TEMPERATURE_SENSOR, s->{
                    for (Sensor<?> sensor : apparatus.sensors()) {
                        if ( sensor instanceof TemperatureSensor ) {
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.CURRENT_TEMPERATURE, c->{
                                //c.setStoredValue();
                                c.setStoredValue(0.0);
                                c.setPermissions(Permission.PAIRED_READ, Permission.NOTIFY);
                                ((TemperatureSensor)sensor).onChange( (t)->{
                                    c.updateValue(t.celsius());
                                });
                            });
                            s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, c-> {
                                c.setStoredValue(sensor.key());
                                c.setPermissions(Permission.PAIRED_READ);
                            });
                        }
                    }
                });
            });
            bridge.addAccessory(accessory);
        }
    }

    private final Bridge bridge;
    private final AtomicInteger aid = new AtomicInteger(1);
    private final AtomicInteger iid = new AtomicInteger(11);
}
