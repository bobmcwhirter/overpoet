package io.overpoet.hap.test;

import java.net.InetSocketAddress;

import io.overpoet.hap.client.Connection;
import io.overpoet.hap.client.PairedConnection;
import io.overpoet.hap.client.SimpleClient;
import io.overpoet.hap.client.auth.simple.SimpleClientAuthStorage;
import io.overpoet.hap.server.HAPServer;
import io.overpoet.hap.server.auth.simple.SimpleServerAuthStorage;
import org.junit.Test;

public class ClientServerTest {

    /*
    @Test
    public void testInteraction() throws Exception {

        SimpleServerAuthStorage serverAuthStorage = new SimpleServerAuthStorage("867-53-0999", "16:AA:8D:FF:CC:B6");
        HAPServer server = new HAPServer(serverAuthStorage);
        InetSocketAddress bind = new InetSocketAddress(0);
        int port = server.start(bind);

        SimpleClientAuthStorage clientAuthStorage = new SimpleClientAuthStorage("16:BB:CC:81:42:42");
        SimpleClient client = new SimpleClient(clientAuthStorage);
        Connection connected = client.connect(new InetSocketAddress(port));
        PairedConnection paired = connected.pair("867-53-0999");

        System.err.println( "accessories: " + paired.accessories());
    }
     */
}
