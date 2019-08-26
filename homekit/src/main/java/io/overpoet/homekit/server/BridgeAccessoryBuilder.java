package io.overpoet.homekit.server;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.common.model.Characteristic;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.hap.server.model.impl.ServerAccessoryImpl;

import static io.overpoet.hap.common.model.Permission.PAIRED_READ;
import static io.overpoet.hap.common.model.Permission.PAIRED_WRITE;

public class BridgeAccessoryBuilder {

    public static ServerAccessory build() {
        AtomicInteger iid = new AtomicInteger(0);
        ServerAccessoryImpl accessory = new ServerAccessoryImpl(1, (a) -> {
            a.addService(iid.incrementAndGet(), Services.ACCESSORY_INFORMATION, (s) -> {
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.MANUFACTURER, (c)->{
                    c.setStoredValue("overpoet");
                    c.setPermissions(PAIRED_READ);
                });
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.MODEL, (c)->{
                    c.setStoredValue("opb");
                    c.setPermissions(PAIRED_READ);
                });
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, (c)->{
                    c.setStoredValue("overpoetbridge");
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
            a.addService(iid.incrementAndGet(), Services.PROTOCOL_INFORMATION, (s) -> {
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.VERSION, (c)->{
                    c.setStoredValue("1.1.0");
                    c.setPermissions(PAIRED_READ);
                });
            });
        });
        return accessory;
    }

}
