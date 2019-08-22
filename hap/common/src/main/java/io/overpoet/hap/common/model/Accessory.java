package io.overpoet.hap.common.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by bob on 9/10/18.
 */
public interface Accessory {
    int getAID();
    List<? extends Service> getServices();
    Optional<? extends Service> findService(int iid);
    Optional<Characteristic> findCharacteristic(int iid);

    String getManufacturer();
    String getModel();
    String getName();
    String getHardwareRevision();
    String getFirmwareRevision();
}
