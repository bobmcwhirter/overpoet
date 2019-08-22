package io.overpoet.homekit.server;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.common.model.Accessory;
import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.hap.server.model.impl.ServerAccessoryImpl;

public class BridgeAccessoryBuilder {

    public static ServerAccessory build() {
        AtomicInteger iid = new AtomicInteger(0);
        ServerAccessoryImpl accessory = new ServerAccessoryImpl(1, (a) -> {
            a.addService(iid.incrementAndGet(), Services.ACCESSORY_INFORMATION, (s) -> {
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.MANUFACTURER, "overpoet");
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.MODEL, "overpoet-bridge");
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.NAME, "overpoet-bridge");
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.SERIAL_NUMBER, "01");
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.FIRMWARE_REVISION, "1");
                s.addCharacteristic(iid.incrementAndGet(), Characteristics.IDENTIFY, Boolean.FALSE);
            });
        });
        return accessory;
    }

}
