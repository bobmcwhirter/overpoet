package io.overpoet.hap.client;

import java.io.IOException;

import io.overpoet.hap.common.model.EventableCharacteristic;
import io.overpoet.hap.client.auth.ClientAuthStorage;
import io.overpoet.hap.client.model.AccessoryDB;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by bob on 8/31/18.
 */
@Ignore
public class ActualDeviceTest {

    /*
    @Before
    public void setUp() throws IOException {
        this.authStorage = ClientAuthStorage.filesystem("footbridge");
    }

    @Test
    public void what() throws Exception {
        //monitor();
        toggle();
    }

    protected void monitor() throws Exception {
        SimpleClient client = new SimpleClient(this.authStorage);
        //Connection conn = client.connect("192.168.1.147", 80);
        Connection conn = client.connect("16:BD:8D:FE:CC:B6");
        PairedConnection pairedConn = conn.pairVerify();

        AccessoryDB accessoryDB = pairedConn.accessories();
        System.err.println(accessoryDB);
        accessoryDB.find(1)
                .ifPresent( (acc)->{
                    acc.findCharacteristic(11).ifPresent( (chr)->{
                        ((EventableCharacteristic)chr).addListener((c)->{
                            System.err.println( "client-monitor " + c );
                        });
                    });
                });
    }

    protected void toggle() throws Exception {
        SimpleClient client = new SimpleClient(this.authStorage);
        //Connection conn = client.connect("192.168.1.147", 80);
        Connection conn = client.connect("16:BD:8D:FE:CC:B6");
        PairedConnection pairedConn = conn.pairVerify();

        AccessoryDB accessoryDB = pairedConn.accessories();
        System.err.println(accessoryDB);
        accessoryDB.find(1)
                .ifPresent( (acc)->{
                    acc.findCharacteristic(11).ifPresent( (chr)->{
                        System.err.println( "--> " + chr);
                        ((EventableCharacteristic)chr).addListener((c)->{
                            System.err.println( "client-toggle " + c );
                        });
                        chr.updateValue( ! (Boolean)chr.getValue()  );
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });

     */
        /*
        client.connect("192.168.1.147", 80)
                .thenCompose( connection->{
                    System.err.println( "Connected");
                    //return connection.pair("805-39-482");
                    return connection.pairVerify();

                })
                .thenApply( connection->{
                    System.err.println( "Paired!");
                    return null;
                }).get();

    }
    */

    private ClientAuthStorage authStorage;
}
