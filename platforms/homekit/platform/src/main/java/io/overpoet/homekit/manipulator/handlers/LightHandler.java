package io.overpoet.homekit.manipulator.handlers;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.hap.common.model.Characteristics;
import io.overpoet.hap.common.model.Services;
import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.homekit.manipulator.ApparatusHandler;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.aspect.Aspect;

public class LightHandler implements ApparatusHandler {
    @Override
    public void handle(AtomicInteger iid, ServerAccessory a, Apparatus apparatus) {
        a.addService(iid.incrementAndGet(), Services.LIGHTBULB, s -> {
            for (Aspect<?, ?> aspect : apparatus.aspects()) {
                if (aspect.datatype() == Boolean.class) {
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.ON, c -> {
                        c.setStoredValue(false);
                        aspect.sensor(Boolean.class).onChange(c::updateValue);
                        if (aspect.isActuator()) {
                            c.setController((value) -> {
                                aspect.actuator(Boolean.class).actuate(value);
                            });
                        }
                    });
                } else if (aspect.datatype() == Integer.class) {
                    s.addCharacteristic(iid.incrementAndGet(), Characteristics.BRIGHTNESS, c -> {
                        c.setStoredValue(0);
                        aspect.sensor(Integer.class).onChange(c::updateValue);
                        if (aspect.isActuator()) {
                            c.setController((value) -> {
                                aspect.actuator(Integer.class).actuate(value);
                            });
                        }
                    });
                }
            }
        });
    }
}
