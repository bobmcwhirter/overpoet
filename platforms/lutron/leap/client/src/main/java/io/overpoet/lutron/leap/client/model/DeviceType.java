package io.overpoet.lutron.leap.client.model;

import static io.overpoet.lutron.leap.client.model.DeviceCategory.*;

public enum DeviceType {
    WALL_DIMMER(SWITCH),
    PLUG_IN_DIMMER(SWITCH),

    PICO_1_BUTTON(SENSOR),
    PICO_2_BUTTON(SENSOR),
    PICO_2_BUTTON_RAISE_LOWER(SENSOR),
    PICO_3_BUTTON(SENSOR),
    PICO_3_BUTTON_RAISE_LOWER(SENSOR),
    PICO_4_BUTTON(SENSOR),
    PICO_4_BUTTON_SCENE(SENSOR),
    PICO_4_BUTTON_ZONE(SENSOR),
    PICO_4_BUTTON_2_GROUP(SENSOR),
    FOUR_GROUP_REMOTE(SENSOR),
    ;

    DeviceType(DeviceCategory category) {
        this.category = category;
    }

    DeviceCategory category() {
        return this.category;
    }

    public static DeviceType of(String str) {
        if (str.equalsIgnoreCase("WallDimmer")) {
            return WALL_DIMMER;
        }
        if (str.equalsIgnoreCase("PlugInDimmer")) {
            return PLUG_IN_DIMMER;
        }
        if (str.equalsIgnoreCase("Pico1Button")) {
            return PICO_1_BUTTON;
        }
        if (str.equalsIgnoreCase("Pico2Button")) {
            return PICO_2_BUTTON;
        }
        if (str.equalsIgnoreCase("Pico2ButtonRaiseLower")) {
            return PICO_2_BUTTON_RAISE_LOWER;
        }
        if (str.equalsIgnoreCase("Pico3Button")) {
            return PICO_3_BUTTON;
        }
        if (str.equalsIgnoreCase("Pico3ButtonRaiseLower")) {
            return PICO_3_BUTTON_RAISE_LOWER;
        }
        if (str.equalsIgnoreCase("Pico4Button")) {
            return PICO_4_BUTTON;
        }
        if (str.equalsIgnoreCase("Pico4ButtonScene")) {
            return PICO_4_BUTTON_SCENE;
        }
        if (str.equalsIgnoreCase("Pico4ButtonZone")) {
            return PICO_4_BUTTON_ZONE;
        }
        if (str.equalsIgnoreCase("Pico4Button2Group")) {
            return PICO_4_BUTTON_2_GROUP;
        }
        if (str.equalsIgnoreCase("FourGroupRemote")) {
            return FOUR_GROUP_REMOTE;
        }
        return null;
    }

    private final DeviceCategory category;
}
