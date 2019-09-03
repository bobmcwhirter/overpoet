package io.overpoet.hap.client.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import io.overpoet.hap.client.codec.AccessoriesRequest;
import io.overpoet.hap.client.codec.CharacteristicEventsRequest;
import io.overpoet.hap.client.codec.UpdateCharacteristicRequest;
import io.overpoet.hap.client.PairedConnection;
import io.overpoet.hap.client.model.AccessoryDB;
import io.netty.channel.Channel;
import io.overpoet.hap.common.model.EventableCharacteristic;
import io.overpoet.hap.common.model.Characteristic;

/**
 * Created by bob on 8/30/18.
 */
public class PairedConnectionImpl implements PairedConnection {

    public PairedConnectionImpl(Channel channel) {
        this.channel = channel;
    }

    //public void setAccessories(AccessoriesImpl accessories) {
    //this.accessories.set(accessories);
    //}

    @Override
    public AccessoryDB accessories() throws ExecutionException, InterruptedException {
        return this.accessories.updateAndGet((result) -> {
            if (result != null) {
                return result;
            }
            AccessoriesRequest req = new AccessoriesRequest();
            this.channel.pipeline().addLast("request", req);
            this.channel.pipeline().writeAndFlush(req);
            try {
                return req.getFuture().get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            } finally {
                this.channel.pipeline().remove("request");
            }
        });
    }

    void updateValue(Characteristic characteristic, Object value) throws ExecutionException, InterruptedException {
        UpdateCharacteristicRequest req = new UpdateCharacteristicRequest(characteristic, value);
        System.err.println("**** WRITE AND FLUSH");
        this.channel.pipeline().writeAndFlush(req);
        System.err.println("**** WRITE AND FLUSH AWAIT");
        req.getFuture().get();
        System.err.println("**** WRITE AND FLUSH COMPLETE");
    }

    void enableEvents(EventableCharacteristic characteristic) throws ExecutionException, InterruptedException {
        CharacteristicEventsRequest req = new CharacteristicEventsRequest(characteristic, true);
        this.channel.pipeline().writeAndFlush(req);
        req.getFuture().get();
    }

    void disableEvents(EventableCharacteristic characteristic) throws ExecutionException, InterruptedException {
        CharacteristicEventsRequest req = new CharacteristicEventsRequest(characteristic, false);
        this.channel.pipeline().writeAndFlush(req);
        req.getFuture().get();
    }

    private final Channel channel;

    private AtomicReference<AccessoryDB> accessories = new AtomicReference<>();
}
