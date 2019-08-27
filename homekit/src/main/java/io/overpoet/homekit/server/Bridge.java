package io.overpoet.homekit.server;

import java.util.concurrent.atomic.AtomicInteger;

import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.hap.server.model.ServerAccessory;
import io.overpoet.hap.server.model.impl.ServerAccessoryDatabaseImpl;

public class Bridge extends ServerAccessoryDatabaseImpl {

    public Bridge(){
        addAccessory( BridgeAccessoryBuilder.build(this.iid));
    }

    void add(Apparatus apparatus) {

    }

    private ServerAccessory bridge;
    private AtomicInteger iid = new AtomicInteger();
}
