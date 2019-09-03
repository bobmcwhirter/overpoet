package io.overpoet.hap.client;

import java.util.concurrent.ExecutionException;

import io.overpoet.hap.client.impl.EventableCharacteristicImpl;
import io.overpoet.hap.client.model.AccessoryDB;

/**
 * Created by bob on 8/30/18.
 */
public interface PairedConnection {
    AccessoryDB accessories() throws ExecutionException, InterruptedException;
}
