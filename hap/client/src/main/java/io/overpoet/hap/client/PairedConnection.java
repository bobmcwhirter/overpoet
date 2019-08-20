package io.overpoet.hap.client;

import java.util.concurrent.ExecutionException;

import io.overpoet.hap.client.model.Accessories;

/**
 * Created by bob on 8/30/18.
 */
public interface PairedConnection {
    Accessories accessories() throws ExecutionException, InterruptedException;
}
