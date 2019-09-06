package io.overpoet.lutron.client;

import java.net.InetAddress;

import io.overpoet.lutron.leap.client.Client;
import io.overpoet.lutron.leap.client.model.Universe;
import org.junit.Ignore;
import org.junit.Test;

public class ClientTest {

    @Test
    @Ignore
    public void testLoadCert() throws Exception {
        Universe universe = new Universe();
        Client client = new Client(universe);

        client.connect(InetAddress.getByName("192.168.1.167"));

        Thread.sleep(8000);

        /*
        Certificate cert = null;

        cert = client.certificate(Paths.get("/Users/bob/lutron/caseta-bridge.crt"));
        System.err.println( cert );

        cert = client.certificate(Paths.get("/Users/bob/lutron/caseta.crt"));
        System.err.println( cert );

        //cert = client.key(Paths.get("/Users/bob/lutron/caseta.key"));
        RSAPrivateKey key = client.key(Paths.get("/Users/bob/lutron/caseta.key"));
        System.err.println( key );
        //System.err.println( cert );
        TrustManager[] tm = client.trustManagers();
        for (TrustManager each : tm) {
            System.err.println( "tm: " + each );
        }

        KeyManager[] km = client.keyManagers();
        for (KeyManager each : km) {
            System.err.println( "km: " + each);

        }
         */


    }
}
