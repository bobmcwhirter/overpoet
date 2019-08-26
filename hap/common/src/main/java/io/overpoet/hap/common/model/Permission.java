package io.overpoet.hap.common.model;

/**
 * Created by bob on 9/14/18.
 */
public enum Permission {
    PAIRED_READ("pr"),
    PAIRED_WRITE("pw"),
    NOTIFY("ev"),
    ADDITIONAL_AUTHORIZATION("aa"),
    TIMED_WRITE("tw"),
    HIDDEN("h");

    Permission(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return this.str;
    }

    private String str;
}
