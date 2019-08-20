package com.overpoet.hap.client.auth;

/**
 * Created by bob on 8/28/18.
 */
public interface AccessoryAuthInfo {

    String getIdentifier();
    String getPin();
    byte[] getKey();

}
