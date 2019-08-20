package io.overpoet.hap.client;

import io.overpoet.hap.client.auth.ClientAuthStorage;

public class ActiveClient {

    public ActiveClient(ClientAuthStorage authStorage) {
        this.authStorage = authStorage;
        this.client = new SimpleClient(authStorage);
    }

    private final ClientAuthStorage authStorage;

    private final SimpleClient client;
}
