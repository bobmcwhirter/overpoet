package io.overpoet.hap.common;

import io.overpoet.hap.common.model.Characteristics;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CharacteristicTest {

    @Test
    public void testShortenedType() {
        assertThat( Characteristics.MANUFACTURER.getEncodedType() ).isEqualTo("20");

    }
}
