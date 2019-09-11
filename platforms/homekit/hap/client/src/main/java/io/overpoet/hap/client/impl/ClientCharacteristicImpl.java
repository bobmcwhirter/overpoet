package io.overpoet.hap.client.impl;

import java.util.concurrent.ExecutionException;

import io.overpoet.hap.client.model.ClientAccessory;
import io.overpoet.hap.common.model.CharacteristicType;
import io.overpoet.hap.common.model.Service;
import io.overpoet.hap.common.model.impl.AbstractCharacteristicImpl;

public class ClientCharacteristicImpl extends AbstractCharacteristicImpl  {
    public ClientCharacteristicImpl(Service service, int iid, CharacteristicType type) {
        super( service, iid, type);
    }

    public void updateValue(Object value) {
        System.err.println( "SHOOT AN UPDATE of " + this + " -> " + value);
        try {
            ((PairedConnectionImpl)((ClientAccessory)getService().getAccessory()).getAccessoriesDB().getPairedConnection()).updateValue(this, value);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestValueUpdate(Object value) {

    }
}
