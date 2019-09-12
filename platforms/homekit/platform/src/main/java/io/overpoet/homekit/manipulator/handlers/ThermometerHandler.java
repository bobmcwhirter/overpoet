package io.overpoet.homekit.manipulator.handlers;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.homekit.manipulator.ApparatusHandler;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.aspect.Aspect;
import io.overpoet.spi.measurement.Temperature;

public class ThermometerHandler implements ApparatusHandler {

    @Override
    public void handle(AtomicInteger iid, ServerAccessory a, Apparatus apparatus) {
        a.addService(iid.incrementAndGet(), Services.TEMPERATURE_SENSOR, s -> {
            for (Aspect<?, ?> aspect : apparatus.aspects()) {
                if (aspect.datatype() == Temperature.class) {
                    if (aspect.isSensor()) {
                        s.addCharacteristic(iid.incrementAndGet(), Characteristics.CURRENT_TEMPERATURE, c -> {
                            c.setStoredValue(0.0);
                            aspect.sensor(Temperature.class).onChange((t) -> {
                                c.updateValue(t.celsius());
                            });
                        });
                    }
                }
            }
        });

    }

}
