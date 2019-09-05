package io.overpoet.lutron.caseta.bridge;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.security.cert.Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.junit.Test;

public class ClientTest {

    @Test
    public void testLoadCert() throws Exception {
        Client client = new Client();

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
