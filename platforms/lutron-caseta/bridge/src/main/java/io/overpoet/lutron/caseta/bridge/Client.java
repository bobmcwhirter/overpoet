package io.overpoet.lutron.caseta.bridge;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.overpoet.lutron.caseta.bridge.codec.ClientInitializer;
import io.overpoet.lutron.caseta.bridge.protocol.GetAreas;
import io.overpoet.lutron.caseta.bridge.protocol.GetDevices;
import io.overpoet.lutron.caseta.bridge.protocol.GetZones;

public class Client {

    public static int DEFAULT_PORT = 8081;

    public Client() {

    }

    public void connect(InetAddress address) throws Exception {
        Bootstrap b = new Bootstrap(); // (1)
        b.group(new NioEventLoopGroup());
        b.channel(NioSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)

        //SSLContext sslContext = SSLContext.getInstance("TLS");
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

        KeyManager[] keyManagers = keyManagers();
        TrustManager[] trustManagers = trustManagers();
        sslContext.init(keyManagers, trustManagers, null);
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
        b.handler(new ClientInitializer(sslEngine));
        // Start the client.
        this.channel = b.connect(address,
                                 DEFAULT_PORT).sync().channel();

        //ByteBuf buf = this.channel.alloc().buffer();
        //buf.writeCharSequence("{\"CommuniqueType\": \"ReadRequest\", \"Header\": {\"Url\": \"/device\"}}", Charset.forName("UTF-8"));
        //buf.writeByte('\r');
        //buf.writeByte('\n');
        //this.channel.pipeline().writeAndFlush(buf);
        this.channel.pipeline().writeAndFlush(new GetZones());
        this.channel.pipeline().writeAndFlush(new GetAreas());
        this.channel.pipeline().writeAndFlush(new GetDevices());
    }

    KeyManager[] keyManagers() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, InvalidKeySpecException, UnrecoverableKeyException {
        char[] password = new char[0];
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        Certificate[] chain = certChain(Paths.get("/Users/bob/lutron/caseta.crt"));
        keyStore.setKeyEntry("caseta", key(Paths.get("/Users/bob/lutron/caseta.key")), password, chain);
        //KeyManagerFactory kmf = KeyManagerFactory.getInstance("RSA");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, password);
        return kmf.getKeyManagers();
    }

    TrustManager[] trustManagers() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("caseta-bridge", caCertificate(Paths.get("/Users/bob/lutron/caseta-bridge.crt")));
        //keyStore.setCertificateEntry( "caseta", certificate(Paths.get("/Users/bob/lutron/caseta.crt")));
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        return tmf.getTrustManagers();
    }

    Certificate caCertificate(Path path) throws CertificateException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try (InputStream in = new FileInputStream(path.toFile())) {
            return factory.generateCertificate(in);
        }
    }

    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";

    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    Certificate[] certChain(Path path) throws CertificateException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try (InputStream in = new FileInputStream(path.toFile())) {
            Collection<? extends Certificate> certs = factory.generateCertificates(in);
            return certs.toArray(new Certificate[0]);
        }
    }

    RSAPrivateKey key(Path path) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String content = Files.readString(path);
        content = content.replaceFirst("-----BEGIN PRIVATE KEY-----\n", "");
        content = content.replaceFirst("-----END PRIVATE KEY-----", "");
        byte[] decoded = Base64.getMimeDecoder().decode(content);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) factory.generatePrivate(keySpec);

        //try (InputStream in = new FileInputStream(path.toFile())) {
        //KeySpec keySpec = new RSAPrivateKeySpec();
        //return factory.generatePrivate(keySpec);
        //}

    }

    private Channel channel;
}
