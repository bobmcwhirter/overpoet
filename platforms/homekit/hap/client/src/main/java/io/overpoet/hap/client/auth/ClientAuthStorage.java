package io.overpoet.hap.client.auth;

import java.io.IOException;

import io.overpoet.hap.client.auth.fs.FilesystemClientAuthStorage;
import io.overpoet.hap.client.auth.simple.SimpleClientAuthStorage;
import io.overpoet.hap.common.spi.AuthStorage;

/**
 * Created by bob on 8/28/18.
 */
public interface ClientAuthStorage extends AuthStorage {

    String getIdentifier();

    AccessoryAuthInfo get(String identifier);
    void put(String identifier, String pin, byte[] ltpk) throws IOException;

    static ClientAuthStorage inMemory(String identifier) {
        return new SimpleClientAuthStorage(identifier);
    }

    static ClientAuthStorage filesystem(String identifier) throws IOException {
        return new FilesystemClientAuthStorage(identifier);
    }

}
