package io.overpoet.homekit.server;

import javax.json.JsonObjectBuilder;

import org.junit.Test;

public class BridgeAccessoryBuilderTest {

    @Test
    public void testJSON() {
        JsonObjectBuilder json = BridgeAccessoryBuilder.build().toJSON();
    }
}
