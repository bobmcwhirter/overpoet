package io.overpoet.homekit.ext;

import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Characteristics;

public class EveCharacteristics extends Characteristics {
    public static final CharacteristicType WIND_SPEED = register("Wind Speed", c->c
            .uuid("49C8AE5A-A3A5-41AB-BF1F-12D5654F9F41")
            .type("eve.wind.speed")
            .intFormat(0, 999, 1)
    );

    public static final CharacteristicType WIND_DIRECTION = register("Wind Direction", c->c
            .uuid("46f1284c-1912-421b-82f5-eb75008b167e")
            .type("eve.wind.direction")
            .intFormat(0, 360, 1)
    );
}
