package client;

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by armin1215 on 4/29/16.
 */
public class ClientListener implements Runnable {
    private DatagramSocket _sock;
    private boolean _running = true;

    public ClientListener(DatagramSocket sock) {
        _sock = sock;
    }
    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    terminate();
                }

            });

            while (_running) {
                if (Thread.interrupted()) terminate();
                byte[] buffer = new byte[4*1024];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

                _sock.receive(dp);
                ClientParser.printClientOutput(new Decoder(dp.getData()));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASN1DecoderFail asn1DecoderFail) {
            asn1DecoderFail.printStackTrace();
        }
    }

    public void terminate() {
        _running = false;
    }
}
