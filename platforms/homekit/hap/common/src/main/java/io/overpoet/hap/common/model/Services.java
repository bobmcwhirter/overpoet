package io.overpoet.hap.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import io.overpoet.hap.common.model.Characteristics;

public class Services {
    private static final Map<UUID, ServiceType> INDEX = new HashMap<>();

    public static ServiceType lookup(String uuid) {
        return lookup(ServiceTypeBuilder.toUUID(uuid));
    }

    public static ServiceType lookup(UUID uuid) {
        return INDEX.get(uuid);
    }

    static ServiceType register(String name, Consumer<ServiceTypeBuilder> config) {
        ServiceType type = ServiceTypeBuilder.configure(name, config);
        INDEX.put(type.getUUID(), type);
        return type;
    }

    public static final ServiceType ACCESSORY_INFORMATION = register("Accessory Information", s -> s
            .uuid("3E")
            .type("accessory-information")
            .required(Characteristics.IDENTIFY)
            .required(Characteristics.MANUFACTURER)
            .required(Characteristics.MODEL)
            .required(Characteristics.NAME)
            .required(Characteristics.FIRMWARE_REVISION)
            .optional(Characteristics.HARDWARE_REVISION)
    );

    public static final ServiceType PROTOCOL_INFORMATION = register("Protocol Information", s -> s
            .uuid("A2")
            .type("protocol.information.service")
            .required(Characteristics.VERSION)
    );

    public static final ServiceType SWITCH = register("Switch", s -> s
            .uuid("49")
            .type("switch")
            .required(Characteristics.ON)
            .optional(Characteristics.NAME)
    );

    public static final ServiceType LIGHTBULB = register("Lightbulb", s -> s
            .uuid("43")
            .type("lightbulb")
            .required(Characteristics.ON)
            .optional(Characteristics.BRIGHTNESS)
            .optional(Characteristics.HUE)
            .optional(Characteristics.SATURATION)
    );

    public static final ServiceType TEMPERATURE_SENSOR = register("Temperature Sensor", s -> s
            .uuid("8A")
            .type("sensor.temperature")
            .required(Characteristics.CURRENT_TEMPERATURE)
    );
}
