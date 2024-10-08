package io.overpoet.hap.client.codec;

import java.util.concurrent.CompletableFuture;

import io.overpoet.hap.client.PairedConnection;

/**
 * Created by bob on 8/27/18.
 */
public class VerifyStartRequest {

    public VerifyStartRequest(CompletableFuture<PairedConnection> future) {
        this.future = future;
    }

    public CompletableFuture<PairedConnection> getFuture() {
        return this.future;
    }

    private final CompletableFuture<PairedConnection> future;

}
